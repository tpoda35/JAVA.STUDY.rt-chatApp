'use strict';

const friendRequestList = document.getElementById('friendRequestList');

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
            toastr.success('Friend Request sent.');
        });
});

function appendRequest(request) {
    const listItem = document.createElement('li');
    listItem.classList.add('list-group-item');
    listItem.classList.add('bg-dark');
    listItem.classList.add('text-white');
    listItem.classList.add('border-0');
    listItem.textContent = request.sentBy;
    listItem.id = request.id;

    friendRequestList.appendChild(listItem);
}