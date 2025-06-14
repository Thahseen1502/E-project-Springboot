//document.addEventListener("DOMContentLoaded", () => {
//    fetchUsers();
//    document.getElementById("userForm").addEventListener("submit", handleUserSubmit);
//});
//
////const apiUrl = "https://localhost:8888/api/users";
//const apiUrl = "/api/users";
//
//
//async function fetchUsers() {
//    const response = await fetch(apiUrl);
//    const users = await response.json();
//
//    const usersContainer = document.getElementById("usersContainer");
//    usersContainer.innerHTML = "";
//
//    users.forEach(user => {
//        const row = document.createElement("tr");
//        row.innerHTML = `
//            <td>${user.id}</td>
//            <td>${user.username}</td>
//            <td>${user.email}</td>
//            <td>${user.phone || "N/A"}</td>
//            <td>${user.address || "N/A"}</td>
//            <td>${user.role || "USER"}</td>
//            <td>
//                <button class="edit-btn" onclick="editUser(${user.id})">Edit</button>
//                <button class="delete-btn" onclick="deleteUser(${user.id})">Delete</button>
//            </td>
//        `;
//        usersContainer.appendChild(row);
//    });
//}
//
//
//async function handleUserSubmit(event) {
//    event.preventDefault();
//
//    const userId = document.getElementById("userId").value;
//    const userData = {
//        username: document.getElementById("username").value,
//        email: document.getElementById("email").value,
//        password: document.getElementById("password").value,
//        phone: document.getElementById("phone").value,
//        address: document.getElementById("address").value,
//        role: "USER"
//    };
//
//    if (userId) {
//        await updateUser(userId, userData);
//    } else {
//        await createUser(userData);
//    }
//
//    document.getElementById("userForm").reset();
//    document.getElementById("userId").value = "";
//
//}
//
//
//async function createUser(userData) {
//    await fetch(apiUrl, {
//        method: "POST",
//        headers: { "Content-Type": "application/json" },
//        body: JSON.stringify(userData)
//    });
//
//    alert("User created successfully!");
//    fetchUsers();
//}
//
//async function updateUser(id, userData) {
//    await fetch(`${apiUrl}/${id}`, {
//        method: "PUT",
//        headers: { "Content-Type": "application/json" },
//        body: JSON.stringify(userData)
//    });
//
//    alert("User updated successfully!");
//    fetchUsers();
//
//}
//
//async function deleteUser(id) {
//    if (confirm("Are you sure you want to delete this user?")) {
//        await fetch(`${apiUrl}/${id}`, { method: "DELETE" });
//
//        alert("User deleted successfully!");
//        fetchUsers();
//    }
//}
//
//async function editUser(id) {
//    const response = await fetch(`${apiUrl}/${id}`);
//    const user = await response.json();
//
//    document.getElementById("userId").value = user.id;
//    document.getElementById("username").value = user.username;
//    document.getElementById("email").value = user.email;
//    document.getElementById("password").value = user.password || "";
//    document.getElementById("phone").value = user.phone || "";
//    document.getElementById("address").value = user.address || "";
//}
//


  const apiUrl = "/api/users";
//  const apiUrl = "https://your-api-gateway.onrender.com/api/users";


  document.addEventListener("DOMContentLoaded", () => {
      fetchUsers();
      document.getElementById("userForm").addEventListener("submit", handleUserSubmit);
  });

  async function fetchUsers() {
      try {
          const response = await fetch(apiUrl);
          if (!response.ok) throw new Error("Failed to fetch users");

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
      } catch (error) {
          console.error("Error loading users:", error);
          alert("Error loading users: " + error.message);
      }
  }

  async function handleUserSubmit(event) {
      event.preventDefault();

      const userId = document.getElementById("userId").value;
      const userData = {
          username: document.getElementById("username").value.trim(),
          email: document.getElementById("email").value.trim(),
          password: document.getElementById("password").value.trim(),
          phone: document.getElementById("phone").value.trim(),
          address: document.getElementById("address").value.trim(),
          role: "USER"
      };

      try {
          if (userId) {
              await updateUser(userId, userData);
              alert("User updated successfully!");
          } else {
              await createUser(userData);
              alert("User created successfully!");
          }

          document.getElementById("userForm").reset();
          document.getElementById("userId").value = "";
          fetchUsers();

      } catch (error) {
          console.error("Error submitting form:", error);
          alert("Failed to save user: " + error.message);
      }
  }

  async function createUser(userData) {
      const response = await fetch(apiUrl, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(userData)
      });

      if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText || "Failed to create user");
      }
  }

  async function updateUser(id, userData) {
      const response = await fetch(`${apiUrl}/${id}`, {
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(userData)
      });

      if (!response.ok) {
          const errorText = await response.text();
          throw new Error(errorText || "Failed to update user");
      }
  }

  async function deleteUser(id) {
      if (!confirm("Are you sure you want to delete this user?")) return;

      try {
          const response = await fetch(`${apiUrl}/${id}`, { method: "DELETE" });

          if (!response.ok) {
              const errorText = await response.text();
              throw new Error(errorText || "Failed to delete user");
          }

          alert("User deleted successfully!");
          fetchUsers();

      } catch (error) {
          console.error("Error deleting user:", error);
          alert("Failed to delete user: " + error.message);
      }
  }

  async function editUser(id) {
      try {
          const response = await fetch(`${apiUrl}/${id}`);
          if (!response.ok) throw new Error("User not found");

          const user = await response.json();

          document.getElementById("userId").value = user.id;
          document.getElementById("username").value = user.username;
          document.getElementById("email").value = user.email;
          document.getElementById("password").value = user.password || "";
          document.getElementById("phone").value = user.phone || "";
          document.getElementById("address").value = user.address || "";

      } catch (error) {
          console.error("Error loading user for edit:", error);
          alert("Failed to load user details: " + error.message);
      }
  }

