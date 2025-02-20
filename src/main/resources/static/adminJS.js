const URL = "http://localhost:8080/api";
const URL_INFO = "http://localhost:8080/admin";

const roleList = []
$(document).ready( function () {
    getAllUsers();
    fetch(URL + '/roles')
        .then(response => response.json())
        .then(roles => {
            roles.forEach(role => {
                roleList.push(role)
            })
        })
})

function showRoles(form) {
    $(`[name="roles"]`, form).empty();
    roleList.forEach(role => {
        let option = `<option value="${role.id}">
                                 ${role.name.replace(/^ROLE_/, '')}
                            </option>`
        $(`[name="roles"]`, form).append(option)
    })
}

function getRole(form) {
    let role = []
    let options = $(`[name="roles"]`, form)[0].options
    for (let i = 0; i < options.length; i++) {
        if (options[i].selected) {
            role.push(roleList[i])
        }
    }
    return role
}

function getAllUsers() {
    const usersTable = $('.users-table')
    usersTable.empty()
    fetch(URL + '/user')
        .then(response => response.json())
        .then(users => {
            users.forEach(user => {
                let row = `$(
                    <tr>
                        <td style="text-align: center;">${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.surname}</td>
                        <td style="text-align: center;">${user.age}</td>
                        <td>${user.email}</td>
                        <td style="text-align: center;">${user.roles.map(r => r.name.substring(5))}</td>  
                        <td style="text-align: center;">
                            <button type="button" class="btn btn-info text-white" data-bs-toggle="modal"
                            onclick="editModal(${user.id})">Edit</button>
                        </td>
                        <td style="text-align: center;">
                            <button type="button" class="btn btn-danger" data-bs-toggle="modal" 
                            onclick="deleteModal(${user.id})">Delete</button>
                        </td>
                    </tr>
                )`
                usersTable.append(row)
            })
        })
        .catch(err => console.log(err))
}

function addUser(){
    let newUserForm = $('#new-user-form')[0]
    showRoles(newUserForm);
    newUserForm.addEventListener("submit", (ev) => {
        ev.preventDefault()
        ev.stopPropagation()

        let addUser = JSON.stringify({
            email:  $(`[name="email"]` , newUserForm).val(),
            firstName:  $(`[name="firstName"]` , newUserForm).val(),
            lastName:  $(`[name="lastName"]` , newUserForm).val(),
            age:  $(`[name="age"]` , newUserForm).val(),
            password:  $(`[name="password"]` , newUserForm).val(),
            roles: getRole(newUserForm)
        })
        fetch(URL + '/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: newUser
        }).then(r => {
            newUserForm.reset()
            if(!r.ok) {
                alert("User with this username already exist!!")
            }else {
                $('#users-table-tab')[0].click()
            }
        })
    })
}

function showModal(form, modal, id) {
    modal.show()
    showRoles(form)
    fetch(URL + `/users/${id}`).then(response => {
        response.json().then(user => {
            $(`[name="email"]`,form).val(user.email)
            $(`[name="id"]`,form).val(user.id)
            $(`[name="firstName"]`,form).val(user.firstName)
            $(`[name="lastName"]`,form).val(user.lastName)
            $(`[name="age"]`,form).val(user.age)
            $(`[name="password"]`,form).val(user.password)
        })
    })
}

function editModal(id) {
    const editModal = new bootstrap.Modal($('.edit-modal'))
    const editForm = $('#edit-form')[0]
    showModal(editForm, editModal, id)
    editForm.addEventListener('submit', (ev) => {
        ev.preventDefault()
        ev.stopPropagation()
        let newUser = JSON.stringify({
            id: $(`[name="id"]` , editForm).val(),
            email:  $(`[name="email"]` , editForm).val(),
            firstName:  $(`[name="firstName"]` , editForm).val(),
            lastName:  $(`[name="lastName"]` , editForm).val(),
            age:  $(`[name="age"]` , editForm).val(),
            password:  $(`[name="password"]` , editForm).val(),
            roles: getRole(editForm)
        })
        fetch(URL + '/users', {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json',
            },
            body: newUser
        }).then(r => {
            editModal.hide()
            $('#users-table-tab')[0].click()
            if(!r.ok) {
                alert("User with this email already exist!!")
            }
        })
    })
}

function deleteModal(id) {
    const deleteModal = new bootstrap.Modal($('.delete-modal'))
    const deleteForm = $('#delete-form')[0]
    showModal(deleteForm, deleteModal, id)
    deleteForm.addEventListener('submit', (ev) => {
        ev.preventDefault()
        ev.stopPropagation()
        fetch(URL + `/users/${$(`[name="id"]` , deleteForm).val()}`, {
            method: 'DELETE'
        }).then(r => {
            deleteModal.hide()
            $('#users-table-tab')[0].click()
            if(!r.ok) {
                alert("Deleting process failed!!")
            }
        })
    })
}

async function getPage() {
    try {
        const response = await fetch(URL_INFO);
        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }

        const user = await response.json();
        getHeader(user);
    } catch (error) {
        console.error(error);
    }
}

function getHeader(user) {
    const usernameElement = document.getElementById("top-username");
    const roleElement = document.getElementById("top-role");

    usernameElement.textContent = user.email;

    let roles = '';
    user.roles.forEach(role => {
        roles += role.name.replace("ROLE_", '') + ' ';
    });

    roleElement.textContent = roles.trim();
}
getPage();