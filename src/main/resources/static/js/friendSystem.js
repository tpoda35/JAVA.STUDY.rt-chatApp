'use strict';

const friendRequestList = document.getElementById('friendRequestList');

document.querySelector('#friendsModal form').addEventListener('submit', function (e) {
    e.preventDefault();

    const friendName = document.querySelector('#friendName').value;

    fetch('http://localhost:8080/api/v1/friends/addFriend?uniqueName=' + encodeURIComponent(friendName), {
        method: 'POST'
    })
        .then(response => {
            if (response.ok) {
                toastr.success('Friend request sent successfully.', 'Action successful.');
                const modal = bootstrap.Modal.getInstance(document.getElementById('friendsModal'));
                modal.hide();
            } else {
                toastr.error('Failed to add a friend.', 'Action failed.');
            }
        })
        .catch(error => console.error('Error:', error));
});

function appendRequest(request) {
    const listItem = document.createElement('li');
    listItem.classList.add('list-group-item');
    listItem.classList.add('bg-dark');
    listItem.classList.add('text-white');
    listItem.classList.add('border-0');
    listItem.textContent = request.sentBy;

    friendRequestList.appendChild(listItem);
}