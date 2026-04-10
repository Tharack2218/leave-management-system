import React from "react";
import AdminDashboard from "./pages/AdminDashboard";
import Login from "./pages/Login";
import EmployeeDashboard from "./pages/EmployeeDashboard";
import Register from "./pages/Register";
import { Routes, Route } from "react-router-dom";



function App() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/admin" element={<AdminDashboard />} />
      <Route path="/employee" element={<EmployeeDashboard />} />
      <Route path="/register" element={<Register />} />
    </Routes>
  );
}

export default App;