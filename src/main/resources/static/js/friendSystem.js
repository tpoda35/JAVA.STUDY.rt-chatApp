'use strict';

const friendRequestList = document.getElementById('friendRequestList');

document.querySelector('#friendModalBtn').addEventListener('click', function() {
    fetch('http://localhost:8080/api/v1/friends/getReceivedRequests', {
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
            friendRequestList.innerHTML = '';
            data.forEach(request => {
               appendRequest(request)
            });
        })
        .catch(error => {
            console.error('Error:', error);
        });
});

document.querySelector('#friendsModal form').addEventListener('submit', function(e) {
    e.preventDefault();
    const friendName = document.querySelector('#friendName').value;

    fetch('http://localhost:8080/api/v1/friends/addFriend?receiverUniqueName=' + encodeURIComponent(friendName), {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    const errorMessage = errorData.message;
                    toastr.error(errorMessage, 'Error');
                    throw new Error(errorMessage);
                });
            }
            return response;
        })
        .then(() => {
            toastr.success('Friend Request sent.', 'Success');
        })
        .catch(error => {
            console.error('Error:', error);
        });
});

function appendRequest(request) {
    const listItem = document.createElement('li');
    listItem.classList.add('list-group-item', 'd-flex', 'justify-content-between', 'align-items-center', 'bg-dark', 'text-white', 'border-0');
    listItem.id = request.id;

    const usernameSpan = document.createElement('span');
    usernameSpan.textContent = request.senderName;

    const buttonContainer = document.createElement('div');

    const acceptButton = document.createElement('button');
    acceptButton.classList.add('btn', 'btn-success', 'btn-sm', 'me-2');
    acceptButton.textContent = 'Accept';
    acceptButton.addEventListener('click', () => handleAccept(request.id));

    const rejectButton = document.createElement('button');
    rejectButton.classList.add('btn', 'btn-danger', 'btn-sm');
    rejectButton.textContent = 'Reject';
    rejectButton.addEventListener('click', () => handleReject(request.id));

    buttonContainer.appendChild(acceptButton);
    buttonContainer.appendChild(rejectButton);

    listItem.appendChild(usernameSpan);
    listItem.appendChild(buttonContainer);

    friendRequestList.appendChild(listItem);
}

function handleAccept(id){
    const listItem = document.getElementById(id);

    fetch('http://localhost:8080/api/v1/friends/acceptRequest/' + id, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    listItem.remove();
                    const errorMessage = errorData.message;
                    toastr.error(errorMessage, 'Error');
                    throw new Error(errorMessage);
                });
            }
            return response;
        })
        .then(() => {
            listItem.remove();
            toastr.success('Request accepted.', 'Success');
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function handleReject(id){
    const listItem = document.getElementById(id);

    fetch('http://localhost:8080/api/v1/friends/rejectRequest/' + id, {
        method: 'POST'
    })
        .then(response => {
            if (!response.ok) {
                return response.json().then(errorData => {
                    listItem.remove();
                    const errorMessage = errorData.message;
                    toastr.error(errorMessage, 'Error');
                    throw new Error(errorMessage);
                });
            }
            return response;
        })
        .then(() => {
            listItem.remove();
            toastr.success('Request rejected.', 'Success');
        })
        .catch(error => {
            console.error('Error:', error);
        });
}