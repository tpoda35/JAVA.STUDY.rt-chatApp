'use strict';

const fullChatArea = document.querySelector('.chat-area');
const chatArea = document.querySelector('#chat-message');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const chatHeaderUsername = document.querySelector('#chat-header-username');

let stompClient = null;
let firstName = null;
let selectedUserId = null;
let userId = null;

connectUser();

async function connectUser(){
    try {
        const response = await fetch('http://localhost:8080/api/v1/users/getUserInfo', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        });

        if (!response.ok) {
            throw new Error('Username not found.');
        }

        const data = await response.json();

        firstName = data.firstname;
        userId = data.userId;

        connect();
    } catch (error) {
        console.error('Error:', error);
    }
}

function connect(event) {
    if (firstName) {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.heartbeat.outgoing = 10000;
        stompClient.heartbeat.incoming = 10000;
        stompClient.connect({}, onConnected, onError);
    }
    if (event) {
        event.preventDefault();
    }
}

function onConnected() {
    stompClient.subscribe(`/user/${userId}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);
    stompClient.subscribe("/topic/online-users", (message) => {
        console.log("User status update:", message.body);
    });

    console.log('Getting connected users...');
    setInterval(sendHeartbeat, 10000);
    findAndDisplayConnectedUsers().then();
}

async function findAndDisplayConnectedUsers() {
    const connectedUsersResponse = await fetch('/users');
    let connectedUsers = await connectedUsersResponse.json();

    connectedUsers = connectedUsers.filter(user => user.id !== userId);
    const connectedUsersList = document.getElementById('connectedUsers');
    connectedUsersList.innerHTML = '';

    connectedUsers.forEach(user => {
        appendUserElement(user, connectedUsersList);
    });
}

function appendUserElement(user, connectedUsersList) {
    const listItem = document.createElement('li');
    listItem.classList.add('user-item');
    listItem.id = user.id;

    const statusDot = document.createElement('span');
    statusDot.classList.add('status-indicator');
    statusDot.classList.add(user.status);
    listItem.appendChild(statusDot);

    const iconImg = document.createElement('img');
    iconImg.src = '../image/user_icon.png';
    iconImg.alt = 'User Icon';
    iconImg.classList.add('user-icon');
    listItem.appendChild(iconImg);

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = user.firstname;

    listItem.appendChild(usernameSpan);
    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    updateChatAreaVisibility();
    const usernameSpan = clickedUser.querySelector('span');
    if (usernameSpan) {
        chatHeaderUsername.textContent = usernameSpan.textContent;
    }

    selectedUserId = clickedUser.getAttribute('id');
    fetchAndDisplayUserChat().then();
}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    messageContainer.classList.add('message');
    if (senderId === userId) {
        messageContainer.classList.add('sender');
    } else {
        messageContainer.classList.add('receiver');
    }
    const message = document.createElement('p');
    message.textContent = content;
    messageContainer.appendChild(message);
    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${userId}/${selectedUserId}`);
    const userChat = await userChatResponse.json();
    console.log(userChat);
    chatArea.innerHTML = '';
    userChat.forEach(chat => {
        displayMessage(chat.senderId, chat.content);
    });
    chatArea.scrollTop = chatArea.scrollHeight;
}

function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function sendMessage(event) {
    const messageContent = messageInput.value.trim();
    if (messageContent && stompClient) {
        const chatMessage = {
            senderId: userId,
            recipientId: selectedUserId,
            content: messageInput.value.trim(),
            timestamp: new Date()
        };
        stompClient.send("/app/chat", {}, JSON.stringify(chatMessage));
        displayMessage(userId, messageInput.value.trim());
        messageInput.value = '';
    }
    chatArea.scrollTop = chatArea.scrollHeight;
    event.preventDefault();
}

async function onMessageReceived(payload) {
    await findAndDisplayConnectedUsers();
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);

    if (Number(selectedUserId) && Number(selectedUserId) === Number(message.senderId)) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }
}

function updateChatAreaVisibility(){
    let activeUser = document.querySelector('.user-item.active');

    if (activeUser !== null){
        fullChatArea.style.display = 'flex';
    } else {
        fullChatArea.style.display = 'none';
    }
}

function sendHeartbeat(){
    if (stompClient && stompClient.connected){
        stompClient.send("/connection/heartbeat", {}, "heartbeat");
        console.log("Heartbeat send.");
    }
}

messageForm.addEventListener('submit', sendMessage, true);