document.addEventListener("DOMContentLoaded", () => {
    fetchUsers();
    document.getElementById("userForm").addEventListener("submit", handleUserSubmit);
});

const apiUrl = "https://localhost:8888/api/users";

async function fetchUsers() {
    const response = await fetch(apiUrl);
    const users = await response.json();

    const usersContainer = document.getElementById("usersContainer");
    usersContainer.innerHTML = "";

    users.forEach(user => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.email}</td>
            <td>${user.phone || "N/A"}</td>
            <td>${user.address || "N/A"}</td>
            <td>${user.role || "USER"}</td>
            <td>
                <button class="edit-btn" onclick="editUser(${user.id})">Edit</button>
                <button class="delete-btn" onclick="deleteUser(${user.id})">Delete</button>
            </td>
        `;
        usersContainer.appendChild(row);
    });
}


async function handleUserSubmit(event) {
    event.preventDefault();

    const userId = document.getElementById("userId").value;
    const userData = {
        username: document.getElementById("username").value,
        email: document.getElementById("email").value,
        password: document.getElementById("password").value,
        phone: document.getElementById("phone").value,
        address: document.getElementById("address").value,
        role: "USER"
    };

    if (userId) {
        await updateUser(userId, userData);
    } else {
        await createUser(userData);
    }

    document.getElementById("userForm").reset();
    document.getElementById("userId").value = "";

}


async function createUser(userData) {
    await fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    });

    alert("User created successfully!");
    fetchUsers();
}

async function updateUser(id, userData) {
    await fetch(`${apiUrl}/${id}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(userData)
    });

    alert("User updated successfully!");
    fetchUsers();

}

async function deleteUser(id) {
    if (confirm("Are you sure you want to delete this user?")) {
        await fetch(`${apiUrl}/${id}`, { method: "DELETE" });

        alert("User deleted successfully!");
        fetchUsers();
    }
}

async function editUser(id) {
    const response = await fetch(`${apiUrl}/${id}`);
    const user = await response.json();

    document.getElementById("userId").value = user.id;
    document.getElementById("username").value = user.username;
    document.getElementById("email").value = user.email;
    document.getElementById("password").value = user.password || "";
    document.getElementById("phone").value = user.phone || "";
    document.getElementById("address").value = user.address || "";
}

