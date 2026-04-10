import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import "./AdminDashboard.css";

function AdminDashboard() {
  const [leaves, setLeaves] = useState([]);
  const [users, setUsers] = useState([]);
  const [employees, setEmployees] = useState([]);
  const [employeeCount, setEmployeeCount] = useState(0);
  const [loadingId, setLoadingId] = useState(null);

  const navigate = useNavigate();

  useEffect(() => {
    fetchLeaves();
    fetchUsers();
    fetchEmployees();
    fetchEmployeeCount();
  }, []);

  const fetchEmployees = async () => {
    const res = await fetch("http://localhost:8089/api/admin/employees");
    const data = await res.json();
    setEmployees(Array.isArray(data) ? data : []);
  };

  const fetchEmployeeCount = async () => {
    const res = await fetch("http://localhost:8089/api/admin/employee-count");
    const data = await res.json();
    setEmployeeCount(typeof data === "number" ? data : 0);
  };

  const fetchLeaves = async () => {
    const res = await fetch("http://localhost:8089/api/admin/leaves");
    const data = await res.json();
    setLeaves(Array.isArray(data) ? data : []);
  };

  const fetchUsers = async () => {
    const res = await fetch("http://localhost:8089/api/admin/pending-users");
    const data = await res.json();
    setUsers(Array.isArray(data) ? data : []);
  };

  const approveUser = async (id) => {
    setLoadingId(id);
    await fetch(`http://localhost:8089/api/admin/approve-user/${id}`, {
      method: "PUT",
    });
    fetchUsers();
    fetchEmployees();
    fetchEmployeeCount();
    setLoadingId(null);
  };

  const approveLeave = async (id) => {
    await fetch(`http://localhost:8089/api/admin/approve-leave/${id}`, {
      method: "PUT",
    });
    fetchLeaves();
  };

  const rejectLeave = async (id) => {
    await fetch(`http://localhost:8089/api/admin/reject-leave/${id}`, {
      method: "PUT",
    });
    fetchLeaves();
  };

  const handleLogout = () => {
    localStorage.removeItem("user");
    navigate("/");
  };

  return (
    <div className="container">

      {/* HEADER */}
      <div className="header">
        <h2>Admin Dashboard</h2>
        <button className="logoutBtn" onClick={handleLogout}>
          Logout
        </button>
      </div>

      {/* DASHBOARD */}
      <div className="dashboard">

        {/* COUNT */}
        <div className="countCard">
          <h3>Total Employees</h3>
          <h1>{employeeCount}</h1>
        </div>

        {/* EMPLOYEES */}
        <div className="card">
          <h3>All Employees</h3>
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {employees.length === 0 ? (
                <tr>
                  <td colSpan="3">No employees</td>
                </tr>
              ) : (
                employees.map((e) => (
                  <tr key={e.id}>
                    <td>{e.name}</td>
                    <td>{e.email}</td>
                    <td>
                      <span className={`status ${e.active ? "approved" : "pending"}`}>
                        {e.active ? "APPROVED" : "PENDING"}
                      </span>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* PENDING USERS */}
        <div className="card">
          <h3>Pending Users</h3>
          <table>
            <thead>
              <tr>
                <th>Name</th>
                <th>Email</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {users.length === 0 ? (
                <tr>
                  <td colSpan="3">No pending users</td>
                </tr>
              ) : (
                users.map((u) => (
                  <tr key={u.id}>
                    <td>{u.name}</td>
                    <td>{u.email}</td>
                    <td>
                      <button
                        className="approveBtn"
                        onClick={() => approveUser(u.id)}
                      >
                        {loadingId === u.id ? "Processing..." : "Approve"}
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

        {/* LEAVES */}
        <div className="card leaveCard">
          <h3>Leave Requests</h3>
          <table>
            <thead>
              <tr>
                <th>Employee</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {leaves.length === 0 ? (
                <tr>
                  <td colSpan="3">No leaves</td>
                </tr>
              ) : (
                leaves.map((l) => (
                  <tr key={l.id}>
                    <td>{l.employeeName}</td>
                    <td>
                      <span className={`status ${l.status.toLowerCase()}`}>
                        {l.status}
                      </span>
                    </td>
                    <td>
                      {l.status === "PENDING" ? (
                        <>
                          <button
                            className="approveBtn"
                            onClick={() => approveLeave(l.id)}
                          >
                            Approve
                          </button>
                          <button
                            className="rejectBtn"
                            onClick={() => rejectLeave(l.id)}
                          >
                            Reject
                          </button>
                        </>
                      ) : (
                        <span className="noAction">No Action</span>
                      )}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>

      </div>
    </div>
  );
}

export default AdminDashboard;

