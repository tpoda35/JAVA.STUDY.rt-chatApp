'use strict';

if (sessionStorage.getItem('displayNameChanged')) {
    toastr.success('Display name changed.', 'Success');
    sessionStorage.removeItem('displayNameChanged');
}