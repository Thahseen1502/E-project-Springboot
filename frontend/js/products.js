//
//// Global variable to store current products
//let currentProducts = [];
//
//// Fetch and display products
//function loadProducts() {
////    fetch('https://localhost:8888/api/products')
//      fetch('/api/products')
//        .then(response => response.json())
//        .then(products => {
//            currentProducts = products; // Store products globally
//            let productList = '';
//            products.forEach(product => {
//                productList += `
//                    <div class="product-card" data-id="${product.id}">
//                        <h3>${product.name}</h3>
//                        <p>${product.description}</p>
//                        <p class="price">Price: ₹${product.price}</p>
//                        <p class="stock">Stock: ${product.stock}</p>
//                        <div class="action-buttons">
//                            <button class="edit-btn" onclick="openEditModal(${product.id})">Edit</button>
//                            <button class="delete-btn" onclick="deleteProduct(${product.id})">Delete</button>
//                        </div>
//                    </div>
//                `;
//            });
//            document.getElementById('products').innerHTML = productList;
//        })
//        .catch(error => console.error('Error fetching products:', error));
//}
//
//// Handle adding a new product
//document.getElementById('product-form').addEventListener('submit', function (e) {
//    e.preventDefault();
//
//    const newProduct = {
//        name: document.getElementById('product-name').value,
//        description: document.getElementById('product-description').value,
//        price: parseFloat(document.getElementById('product-price').value),
//        stock: parseInt(document.getElementById('product-stock').value),
//    };
//
//    fetch('https://localhost:8888/api/products', {
//        method: 'POST',
//        headers: {
//            'Content-Type': 'application/json',
//        },
//        body: JSON.stringify(newProduct),
//    })
//    .then(response => response.json())
//    .then(product => {
//        alert('Product added successfully');
//        // Clear form fields
//        document.getElementById('product-form').reset();
//        loadProducts();  // Reload the product list
//    })
//    .catch(error => console.error('Error adding product:', error));
//});
//
//// Open edit modal and populate with product data
//function openEditModal(productId) {
//    const product = currentProducts.find(p => p.id === productId);
//    if (!product) return;
//
//    // Populate the edit form
//    document.getElementById('edit-id').value = product.id;
//    document.getElementById('edit-name').value = product.name;
//    document.getElementById('edit-description').value = product.description;
//    document.getElementById('edit-price').value = product.price;
//    document.getElementById('edit-stock').value = product.stock;
//
//    // Display the modal
//    document.getElementById('edit-modal').style.display = 'block';
//}
//
//// Handle updating a product
//document.getElementById('edit-form').addEventListener('submit', function(e) {
//    e.preventDefault();
//
//    const productId = document.getElementById('edit-id').value;
//    const updatedProduct = {
//        name: document.getElementById('edit-name').value,
//        description: document.getElementById('edit-description').value,
//        price: parseFloat(document.getElementById('edit-price').value),
//        stock: parseInt(document.getElementById('edit-stock').value),
//    };
//
//    fetch(`https://localhost:8888/api/products/${productId}`, {
//        method: 'PUT',
//        headers: {
//            'Content-Type': 'application/json',
//        },
//        body: JSON.stringify(updatedProduct),
//    })
//    .then(response => response.json())
//    .then(product => {
//        alert('Product updated successfully');
//        // Close the modal and reload products
//        document.getElementById('edit-modal').style.display = 'none';
//        loadProducts();
//    })
//    .catch(error => console.error('Error updating product:', error));
//});
//
//// Handle deleting a product
//function deleteProduct(productId) {
//    if (confirm('Are you sure you want to delete this product?')) {
//        fetch(`https://localhost:8888/api/products/${productId}`, {
//            method: 'DELETE',
//        })
//        .then(response => {
//            if (response.ok) {
//                alert('Product deleted successfully');
//                loadProducts(); // Reload the product list
//            } else {
//                alert('Failed to delete the product');
//            }
//        })
//        .catch(error => console.error('Error deleting product:', error));
//    }
//}
//
//// Close modal when clicking the X
//document.querySelector('.close').addEventListener('click', function() {
//    document.getElementById('edit-modal').style.display = 'none';
//});
//
//// Close modal when clicking outside the content
//window.addEventListener('click', function(event) {
//    const modal = document.getElementById('edit-modal');
//    if (event.target === modal) {
//        modal.style.display = 'none';
//    }
//});
//
//// Initial load
//loadProducts();



  let currentProducts = [];

  document.addEventListener("DOMContentLoaded", () => {
    loadProducts();

    document.getElementById("product-form").addEventListener("submit", handleAddProduct);
    document.getElementById("edit-form").addEventListener("submit", handleEditProduct);

    // Close modal on X click
    document.querySelector(".close").addEventListener("click", () => {
      document.getElementById("edit-modal").style.display = "none";
    });

    // Close modal when clicking outside
    window.addEventListener("click", (event) => {
      const modal = document.getElementById("edit-modal");
      if (event.target === modal) {
        modal.style.display = "none";
      }
    });
  });

//  const apiUrl = "/api/products";
const apiUrl = "https://product-service-e0nd.onrender.com/api/products";
  async function loadProducts() {
    try {
      const response = await fetch(apiUrl);
      if (!response.ok) throw new Error("Failed to fetch products");

      const products = await response.json();
      currentProducts = products;

      const container = document.getElementById("products");
      container.innerHTML = "";

      products.forEach((product) => {
        const card = document.createElement("div");
        card.className = "product-card";
        card.dataset.id = product.id;

        card.innerHTML = `
          <h3>${product.name}</h3>
          <p>${product.description}</p>
          <p class="price">Price: ₹${product.price}</p>
          <p class="stock">Stock: ${product.stock}</p>
          <div class="action-buttons">
              <button class="edit-btn" onclick="openEditModal(${product.id})">Edit</button>
              <button class="delete-btn" onclick="deleteProduct(${product.id})">Delete</button>
          </div>
        `;
        container.appendChild(card);
      });
    } catch (error) {
      console.error("Error loading products:", error);
      alert("Error loading products: " + error.message);
    }
  }

  async function handleAddProduct(e) {
    e.preventDefault();

    const newProduct = {
      name: document.getElementById("product-name").value.trim(),
      description: document.getElementById("product-description").value.trim(),
      price: parseFloat(document.getElementById("product-price").value),
      stock: parseInt(document.getElementById("product-stock").value),
    };

    try {
      const response = await fetch(apiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newProduct),
      });

      if (!response.ok) {
        const text = await response.text();
        throw new Error(text || "Failed to add product");
      }

      alert("Product added successfully!");
      document.getElementById("product-form").reset();
      loadProducts();
    } catch (error) {
      console.error("Error adding product:", error);
      alert("Failed to add product: " + error.message);
    }
  }

  function openEditModal(productId) {
    const product = currentProducts.find((p) => p.id === productId);
    if (!product) return;

    document.getElementById("edit-id").value = product.id;
    document.getElementById("edit-name").value = product.name;
    document.getElementById("edit-description").value = product.description;
    document.getElementById("edit-price").value = product.price;
    document.getElementById("edit-stock").value = product.stock;

    document.getElementById("edit-modal").style.display = "block";
  }

  async function handleEditProduct(e) {
    e.preventDefault();

    const productId = document.getElementById("edit-id").value;
    const updatedProduct = {
      name: document.getElementById("edit-name").value.trim(),
      description: document.getElementById("edit-description").value.trim(),
      price: parseFloat(document.getElementById("edit-price").value),
      stock: parseInt(document.getElementById("edit-stock").value),
    };

    try {
      const response = await fetch(`${apiUrl}/${productId}`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedProduct),
      });

      if (!response.ok) {
        const text = await response.text();
        throw new Error(text || "Failed to update product");
      }

      alert("Product updated successfully!");
      document.getElementById("edit-modal").style.display = "none";
      loadProducts();
    } catch (error) {
      console.error("Error updating product:", error);
      alert("Failed to update product: " + error.message);
    }
  }

  async function deleteProduct(productId) {
    if (!confirm("Are you sure you want to delete this product?")) return;

    try {
      const response = await fetch(`${apiUrl}/${productId}`, { method: "DELETE" });

      if (!response.ok) {
        const text = await response.text();
        throw new Error(text || "Failed to delete product");
      }

      alert("Product deleted successfully!");
      loadProducts();
    } catch (error) {
      console.error("Error deleting product:", error);
      alert("Failed to delete product: " + error.message);
    }
  }

