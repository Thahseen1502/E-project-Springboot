  // Configuration
  const API_ENDPOINTS = ['/api'];
  let API_BASE_URL = '/api';

  // Test API endpoints to find working one
  async function findWorkingEndpoint() {
      for (const endpoint of API_ENDPOINTS) {
          try {
              const response = await fetch(`${endpoint}/orders`, {
                  method: 'GET',
                  headers: {
                      'Accept': 'application/json',
                      'Content-Type': 'application/json'
                  }
              });
              if (response.ok || response.status === 404) { // 404 is OK for empty orders
                  API_BASE_URL = endpoint;
                  console.log('Connected to API endpoint:', API_BASE_URL);
                  return endpoint;
              }
          } catch (error) {
              console.log(`Endpoint ${endpoint} failed:`, error.message);
          }
      }
      throw new Error('No working API endpoint found');
  }

  // Utility functions
  function showMessage(message, type = 'success') {
      const messageContainer = document.getElementById('messageContainer');
      const messageDiv = document.createElement('div');
      messageDiv.className = type;
      messageDiv.textContent = message;
      messageContainer.appendChild(messageDiv);

      setTimeout(() => {
          messageDiv.remove();
      }, 5000);
  }

  function formatStatus(status) {
      const statusElement = document.createElement('span');
      statusElement.className = `status ${status.toLowerCase()}`;
      statusElement.textContent = status;
      return statusElement.outerHTML;
  }

  async function apiCall(url, options = {}) {
      try {
          console.log('Making API call to:', url, 'with options:', options);

          const response = await fetch(url, {
              ...options,
              headers: {
                  'Content-Type': 'application/json',
                  'Accept': 'application/json',
                  ...options.headers
              }
          });

          console.log('Response status:', response.status);

          if (!response.ok) {
              const errorText = await response.text();
              console.error('API Error Response:', errorText);
              throw new Error(`HTTP ${response.status}: ${errorText}`);
          }

          const result = await response.json();
          console.log('API Success Response:', result);
          return result;

      } catch (error) {
          console.error('API call failed:', error);
          throw error;
      }
  }

  async function fetchOrders() {
      try {
          const ordersContainer = document.getElementById('ordersContainer');
          ordersContainer.innerHTML = '<div class="loading">Loading orders...</div>';

          const orders = await apiCall(`${API_BASE_URL}/orders`);

          if (!orders || orders.length === 0) {
              ordersContainer.innerHTML = '<div class="loading">No orders found</div>';
              return;
          }

          ordersContainer.innerHTML = '';

          orders.forEach(order => {
              const orderCard = document.createElement('div');
              orderCard.className = 'order-card';
              orderCard.innerHTML = `
                  <h3>Order #${order.id}</h3>
                  <p><strong>User ID:</strong> ${order.userId}</p>
                  <p><strong>Product ID:</strong> ${order.productId}</p>
                  <p><strong>Quantity:</strong> ${order.quantity}</p>
                  <p class="total-price"><strong>Total:</strong> ₹${order.totalPrice || 'N/A'}</p>
                  <p><strong>Status:</strong> ${formatStatus(order.status)}</p>
                  <p><strong>Order Date:</strong> ${order.orderDate ? new Date(order.orderDate).toLocaleString() : 'N/A'}</p>
                  <div class="order-actions">
                      <button class="edit-btn" data-id="${order.id}">Edit</button>
                      <button class="delete-btn" data-id="${order.id}">Delete</button>
                  </div>
              `;
              ordersContainer.appendChild(orderCard);
          });

          document.querySelectorAll(".delete-btn").forEach(btn => {
              btn.addEventListener("click", deleteOrder);
          });

          document.querySelectorAll(".edit-btn").forEach(btn => {
              btn.addEventListener("click", editOrder);
          });

      } catch (error) {
          console.error('Error fetching orders:', error);
          document.getElementById('ordersContainer').innerHTML =
              '<div class="error">Failed to load orders. Please check your connection and try again.</div>';
          showMessage('Failed to load orders: ' + error.message, 'error');
      }
  }

  async function createOrder(event) {
      event.preventDefault();

      try {
          const formData = new FormData(event.target);
          const orderRequestDTO = {
              userId: parseInt(formData.get('userId')),
              productId: parseInt(formData.get('productId')),
              quantity: parseInt(formData.get('quantity'))
          };

          if (!orderRequestDTO.userId || !orderRequestDTO.productId || !orderRequestDTO.quantity) {
              throw new Error('User ID, Product ID, and Quantity are required');
          }

          console.log('Creating order with data:', orderRequestDTO);

          const result = await apiCall(`${API_BASE_URL}/orders`, {
              method: 'POST',
              body: JSON.stringify(orderRequestDTO)
          });

          console.log('Order created successfully:', result);
          showMessage('Order created successfully!');
          document.getElementById("orderForm").reset();
          await fetchOrders();

      } catch (error) {
          console.error('Error creating order:', error);
          showMessage('Failed to create order: ' + error.message, 'error');
      }
  }

  async function deleteOrder(event) {
      const orderId = event.target.getAttribute("data-id");

      if (!confirm("Are you sure you want to delete this order?")) return;

      try {
          await apiCall(`${API_BASE_URL}/orders/${orderId}`, {
              method: 'DELETE'
          });

          showMessage('Order deleted successfully!');
          await fetchOrders();

      } catch (error) {
          console.error('Error deleting order:', error);
          showMessage('Failed to delete order: ' + error.message, 'error');
      }
  }

  async function editOrder(event) {
      const orderId = event.target.getAttribute("data-id");

      try {
          const currentOrder = await apiCall(`${API_BASE_URL}/orders/${orderId}`);

          const userId = prompt("Enter User ID:", currentOrder.userId || '');
          const productId = prompt("Enter Product ID:", currentOrder.productId || '');
          const quantity = prompt("Enter Quantity:", currentOrder.quantity || '');

          if (isNaN(userId) || isNaN(productId) || isNaN(quantity)) {
              showMessage("User ID, Product ID, and Quantity must be numbers.", 'error');
              return;
          }

          const orderRequestDTO = {
              userId: parseInt(userId),
              productId: parseInt(productId),
              quantity: parseInt(quantity)
          };

          console.log('Updating order with data:', orderRequestDTO);

          const result = await apiCall(`${API_BASE_URL}/orders/${orderId}`, {
              method: 'PUT',
              body: JSON.stringify(orderRequestDTO)
          });

          console.log('Order updated successfully:', result);
          showMessage('Order updated successfully!');
          await fetchOrders();

      } catch (error) {
          console.error('Error updating order:', error);
          showMessage('Failed to update order: ' + error.message, 'error');
      }
  }

  document.addEventListener("DOMContentLoaded", async () => {
      try {
          await findWorkingEndpoint();
          await fetchOrders();

          document.getElementById("orderForm").addEventListener("submit", createOrder);
          showMessage(`Connected to API at: ${API_BASE_URL}`, 'success');

      } catch (error) {
          console.error('Initialization failed:', error);
          showMessage('Failed to connect to API. Please ensure your backend services are running.', 'error');
          document.getElementById('ordersContainer').innerHTML =
              '<div class="error">Cannot connect to backend services. Please check if your Docker containers are running.</div>';
      }
  });
