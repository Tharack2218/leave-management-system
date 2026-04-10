import React, { useState, useEffect, useCallback } from "react";
import "./EmployeeDashboard.css";

function EmployeeDashboard() {
  const storedUser = JSON.parse(localStorage.getItem("user"));

  const [user, setUser] = useState(null);
  const [reason, setReason] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [loading, setLoading] = useState(false);
  const [leaves, setLeaves] = useState([]);
  const [leaveLimitReached, setLeaveLimitReached] = useState(false);

  // FETCH USER
  useEffect(() => {
    if (!storedUser?.email) return;

    fetch(`http://localhost:8089/api/users/by-email/${storedUser.email}`)
      .then(res => res.json())
      .then(data => setUser(data))
      .catch(err => console.error(err));
  }, [storedUser]);

  // FETCH LEAVES + LIMIT CHECK
  const fetchLeaves = useCallback(() => {
    if (!storedUser?.email) return;

    fetch(`http://localhost:8089/api/leaves/employee/${storedUser.email}`)
      .then(res => res.json())
      .then(data => {
        const leavesData = Array.isArray(data) ? data : [];
        setLeaves(leavesData);

        const now = new Date();

        // ✅ FIX: calculate only valid leaves (ignore REJECTED)
        const approvedLeaves = leavesData
          .filter(l => {
            const d = new Date(l.startDate);
            return (
              l.status !== "REJECTED" &&
              d.getMonth() === now.getMonth() &&
              d.getFullYear() === now.getFullYear()
            );
          })
          .reduce((total, l) => {
            const start = new Date(l.startDate);
            const end = new Date(l.endDate);
            const days =
              (end - start) / (1000 * 60 * 60 * 24) + 1;
            return total + days;
          }, 0);

        // ✅ FIXED HERE
        setLeaveLimitReached(approvedLeaves >= 3);
      })
      .catch(err => console.error(err));
  }, [storedUser]);

  useEffect(() => {
    fetchLeaves();
  }, [fetchLeaves]);

  // APPLY LEAVE
  const applyLeave = async () => {
    if (!reason || !startDate || !endDate) {
      alert("Please fill all fields");
      return;
    }

    if (endDate < startDate) {
      alert("End date cannot be before start date");
      return;
    }

    setLoading(true);

    try {
      const res = await fetch("http://localhost:8089/api/leaves/apply", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          employeeName: user?.name || storedUser.email,
          employeeEmail: storedUser.email,
          startDate,
          endDate,
          reason,
        }),
      });

      const data = await res.text();

      if (!res.ok) {
        alert(data);
        return;
      }

      alert("Leave applied successfully 🎉");
      fetchLeaves();

      setReason("");
      setStartDate("");
      setEndDate("");

    } catch (err) {
      alert("Error applying leave");
    } finally {
      setLoading(false);
    }
  };

  // LOGOUT
  const handleLogout = () => {
    localStorage.removeItem("user");
    window.location.href = "/";
  };

  return (
    <div className="employee-container">
      <div className="employee-card">

        <div className="header">
          <h2>Employee Dashboard</h2>
          <button className="logoutBtn" onClick={handleLogout}>
            Logout
          </button>
        </div>

        <p className="welcome">
          Welcome, <b>{user?.name || storedUser?.email}</b>
        </p>

        {leaveLimitReached && (
          <p className="warning">
            ⚠️ You reached 2 leave days this month. Meet admin.
          </p>
        )}

        <input
          className="input"
          type="date"
          value={startDate}
          onChange={(e) => setStartDate(e.target.value)}
          disabled={leaveLimitReached}
        />

        <input
          className="input"
          type="date"
          value={endDate}
          onChange={(e) => setEndDate(e.target.value)}
          disabled={leaveLimitReached}
        />

        <textarea
          className="textarea"
          placeholder="Reason for leave"
          value={reason}
          onChange={(e) => setReason(e.target.value)}
          disabled={leaveLimitReached}
        />

        <button
          className="applyBtn"
          onClick={applyLeave}
          disabled={loading || leaveLimitReached}
        >
          {leaveLimitReached
            ? "Limit Reached"
            : loading
            ? "Applying..."
            : "Apply Leave"}
        </button>

        <h3>My Leaves</h3>

        <table>
          <thead>
            <tr>
              <th>Start</th>
              <th>End</th>
              <th>Reason</th>
              <th>Status</th>
            </tr>
          </thead>

          <tbody>
            {leaves.length > 0 ? (
              leaves.map((l, i) => (
                <tr key={i}>
                  <td>{l.startDate}</td>
                  <td>{l.endDate}</td>
                  <td>{l.reason}</td>
                  <td className={l.status.toLowerCase()}>
                    {l.status}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="4">No leaves found</td>
              </tr>
            )}
          </tbody>
        </table>

      </div>
    </div>
  );
}

export default EmployeeDashboard;