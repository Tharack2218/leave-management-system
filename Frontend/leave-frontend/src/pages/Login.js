import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleLogin = async () => {
    if (!email || !password) {
      alert("Please enter email and password");
      return;
    }

    setLoading(true);

    try {
      const res = await fetch("http://localhost:8089/api/auth/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ email, password }),
      });

      let data;

      try {
        data = await res.json();
      } catch {
        data = await res.text();
      }

      console.log("Login Response:", data);

      if (!res.ok) {
        alert(data.message || data);
        return;
      }

      // ✅ IMPORTANT: store only needed data
      const userData = {
        email: data.email,
        role: data.role,
        id: data.id, // if available
      };

      localStorage.setItem("user", JSON.stringify(userData));

      // ✅ DEBUG
      console.log("Saved User:", userData);

      // ✅ Use navigate instead of full reload
      if (data.role === "ADMIN") {
        navigate("/admin");
      } else if (data.role === "EMPLOYEE") {
        navigate("/employee");
      } else {
        alert("Invalid role from server");
      }

    } catch (err) {
      console.error("Login error:", err);
      alert("Network error (backend not reachable)");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div style={styles.container}>
      <div style={styles.card}>
        <h2 style={{ marginBottom: "20px" }}>Login</h2>

        <input
          style={styles.input}
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />

        <input
          style={styles.input}
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />

        <button
          style={styles.button}
          onClick={handleLogin}
          disabled={loading}
        >
          {loading ? "Logging in..." : "Login"}
        </button>

        <p style={{ marginTop: "15px" }}>
          New user?{" "}
          <span style={styles.link} onClick={() => navigate("/register")}>
            Register here
          </span>
        </p>
      </div>
    </div>
  );
}

export default Login;



const styles = {
  container: {
    height: "100vh",
    display: "flex",
    justifyContent: "center",
    alignItems: "center",
    background: "#f4f6f8",
  },
  card: {
    padding: "30px",
    borderRadius: "12px",
    background: "white",
    boxShadow: "0 6px 15px rgba(0,0,0,0.1)",
    textAlign: "center",
    width: "300px",
  },
  input: {
    display: "block",
    width: "100%",
    margin: "10px 0",
    padding: "10px",
    borderRadius: "5px",
    border: "1px solid #ccc",
  },
  button: {
    width: "100%",
    padding: "10px",
    background: "#007bff",
    color: "white",
    border: "none",
    borderRadius: "5px",
    cursor: "pointer",
    marginTop: "10px",
  },
  link: {
    color: "#007bff",
    cursor: "pointer",
    fontWeight: "bold",
  },
};