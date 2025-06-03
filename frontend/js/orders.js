document.addEventListener("DOMContentLoaded", () => {
    // Fetch orders from API and display them
    fetchOrders();

    // Handle form submission
    document.getElementById("orderForm").addEventListener("submit", createOrder);
});

// Fetch all orders
async function fetchOrders() {
    const response = await fetch('https://localhost:8888/api/orders');
    const orders = await response.json();

    const ordersContainer = document.getElementById('ordersContainer');
    ordersContainer.innerHTML = '';

    orders.forEach(order => {
        const orderCard = document.createElement('div');
        orderCard.className = 'order-card';
        orderCard.innerHTML = `
            <h3>Order #${order.id}</h3>
            <p>User ID: ${order.userId}</p>
            <p>Product ID: ${order.productId}</p>
            <p>Quantity: ${order.quantity}</p>
            <p class="total-price">Total: ₹${order.totalPrice}</p>
            <p class="status">Status: ${order.status}</p>
            <p class="order-date">Ordered on: ${new Date(order.orderDate).toLocaleString()}</p>
            <button class="edit-btn" data-id="${order.id}">Edit</button>
            <button class="delete-btn" data-id="${order.id}">Delete</button>
        `;
        ordersContainer.appendChild(orderCard);
    });

    // Add event listeners for edit and delete
    document.querySelectorAll(".delete-btn").forEach(btn => {
        btn.addEventListener("click", deleteOrder);
    });

    document.querySelectorAll(".edit-btn").forEach(btn => {
        btn.addEventListener("click", editOrder);
    });
}

// Create a new order
async function createOrder(event) {
    event.preventDefault();

    const userId = document.getElementById('userId').value;
    const productId = document.getElementById('productId').value;
    const quantity = document.getElementById('quantity').value;
    const status = document.getElementById('status').value;

    const newOrder = {
        userId: userId,
        productId: productId,
        quantity: quantity,
        status: status
    };

    const response = await fetch('https://localhost:8888/api/orders', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(newOrder)
    });

    if (response.ok) {
        alert('Order created successfully!');
        document.getElementById("orderForm").reset();
        fetchOrders();
    } else {
        alert('Error creating order!');
    }
}

// Delete an order
async function deleteOrder(event) {
    const orderId = event.target.getAttribute("data-id");
    const confirmDelete = confirm("Are you sure you want to delete this order?");
    if (!confirmDelete) return;

    const response = await fetch(`https://localhost:8888/api/orders/${orderId}`, {
        method: 'DELETE'
    });

    if (response.ok) {
        alert('Order deleted successfully!');
        fetchOrders();
    } else {
        alert('Failed to delete order.');
    }
}

// Edit an order
async function editOrder(event) {
    const orderId = event.target.getAttribute("data-id");

    const userId = prompt("Enter new User ID:");
    const productId = prompt("Enter new Product ID:");
    const quantity = prompt("Enter new Quantity:");
    const status = prompt("Enter new Status:");

    if (!userId || !productId || !quantity || !status) {
        alert("All fields are required for update.");
        return;
    }

    const updatedOrder = {
        userId,
        productId,
        quantity,
        status
    };

    const response = await fetch(`https://localhost:8888/api/orders/${orderId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(updatedOrder)
    });

    if (response.ok) {
        alert('Order updated successfully!');
        fetchOrders();
    } else {
        alert('Failed to update order.');
    }
}
