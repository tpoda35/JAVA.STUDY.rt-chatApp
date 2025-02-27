'use strict';

const fullChatArea = document.querySelector('.index-second-column');
const indexFirstColumn = document.querySelector('.index-first-column');
const chatArea = document.querySelector('#chat-message');
const messageForm = document.querySelector('#messageForm');
const messageInput = document.querySelector('#message');
const connectedUsersList = document.getElementById('connectedUsers');

let stompClient = null;
let dName = null;
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

        dName = data.displayName;
        userId = data.userId;

        connect();
    } catch (error) {
        console.error('Error:', error);
    }
}

function connect(event) {
    if (dName) {
        const socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    if (event) {
        event.preventDefault();
    }
}

function onConnected() {
    stompClient.subscribe(`/user/${userId}/queue/messages`, onMessageReceived);
    stompClient.subscribe(`/user/public`, onMessageReceived);
    stompClient.subscribe(`/user/${userId}/queue/friends`, function (messageOutput) {
        let message = JSON.parse(messageOutput.body);

        if (message.type === 'FRIEND_UPDATE'){
            modifyFriendList(message);

        } else if (message.type === 'FRIEND_ADD') {
            appendUserElement(message);
        } else {
            throw new Error("Invalid type on queue/friends.");
        }
    });

    loadFriendListOnConnect();
}

function modifyFriendList(message) {
    const listItem = document.getElementById(message.userId);
    const statusDot = listItem.querySelector('span');
    statusDot.classList.remove('ONLINE', 'OFFLINE');
    statusDot.classList.add(message.status);
}

function loadFriendListOnConnect() {
    fetch('http://localhost:8080/api/v1/friends/getAllFriend', {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    const errorMessage = errorData.message;
                    throw new Error(errorMessage);
                });
            }
            return response.json();
        })
        .then(data => {
            connectedUsersList.innerHTML = '';
            data.forEach(request => {
                appendUserElement(request)
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function appendUserElement(request) {
    const listItem = document.createElement('li');
    const divItem1 = document.createElement('div');
    const divItem2 = document.createElement('div');
    const statusDot = document.createElement('span');
    const iconImg = document.createElement('img');
    const usernameSpan = document.createElement('span');
    const lastMessage = document.createElement('span');

    listItem.classList.add('user-item');
    listItem.id = request.userId;
    listItem.appendChild(divItem1);

    iconImg.src = '../image/user_icon.png';
    iconImg.alt = 'User Icon';
    iconImg.classList.add('user-icon');
    divItem1.appendChild(iconImg);

    statusDot.classList.add('status-indicator');
    statusDot.classList.add(request.status);
    divItem1.appendChild(statusDot);
    divItem1.appendChild(divItem2);

    usernameSpan.textContent = request.displayName;
    lastMessage.textContent = 'Hey there!';

    divItem2.classList.add('user-item-text');
    divItem2.appendChild(usernameSpan);
    divItem2.appendChild(lastMessage);

    listItem.addEventListener('click', userItemClick);

    connectedUsersList.appendChild(listItem);
}

function userItemClick(event) {
    document.querySelectorAll('.user-item').forEach(item => {
        item.classList.remove('active');
    });

    const clickedUser = event.currentTarget;
    clickedUser.classList.add('active');

    selectedUserId = clickedUser.getAttribute('id');
    indexFirstColumn.classList.remove('w-100');
    fullChatArea.classList.remove('d-none');

    fetchAndDisplayUserChat().then();
}

function displayMessage(senderId, content) {
    const messageContainer = document.createElement('div');
    const message = document.createElement('p');
    const iconImg = document.createElement('img');
    if (senderId === userId) {
        messageContainer.classList.add('d-flex', 'm-3', 'justify-content-start');
        messageContainer.appendChild(iconImg);
        messageContainer.appendChild(message);

        iconImg.src = '../image/user_icon.png';
        iconImg.alt = 'User Icon';
        iconImg.classList.add('chat-icon');

        message.classList.add('sender');
        message.textContent = content;
    } else {
        messageContainer.classList.add('d-flex', 'm-3', 'justify-content-end');
        messageContainer.appendChild(message);
        messageContainer.appendChild(iconImg);

        iconImg.src = '../image/user_icon.png';
        iconImg.alt = 'User Icon';
        iconImg.classList.add('chat-icon');

        message.classList.add('receiver');
        message.textContent = content;
    }

    chatArea.appendChild(messageContainer);
}

async function fetchAndDisplayUserChat() {
    const userChatResponse = await fetch(`/messages/${userId}/${selectedUserId}`);
    const userChat = await userChatResponse.json();

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
    console.log('Message received', payload);
    const message = JSON.parse(payload.body);

    if (Number(selectedUserId) && Number(selectedUserId) === Number(message.senderId)) {
        displayMessage(message.senderId, message.content);
        chatArea.scrollTop = chatArea.scrollHeight;
    }
}

messageForm.addEventListener('submit', sendMessage, true);