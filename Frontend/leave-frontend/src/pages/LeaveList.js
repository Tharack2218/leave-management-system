import React, { useEffect, useState } from "react";

function LeaveList() {
  const [leaves, setLeaves] = useState([]);

  useEffect(() => {
    fetch("http://localhost:8089/api/admin/leaves")
      .then((res) => res.json())
      .then((data) => setLeaves(data));
  }, []);

  const approve = async (id) => {
    await fetch(`http://localhost:8089/api/admin/approve-leave/${id}`, {
      method: "PUT",
    });
    alert("Approved");
    window.location.reload();
  };

  return (
    <div>
      <h2>Leave Requests</h2>

      <table border="1">
        <thead>
          <tr>
            <th>Name</th>
            <th>Reason</th>
            <th>Date</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>

        <tbody>
          {leaves.map((l) => (
            <tr key={l.id}>
              <td>{l.employeeName}</td>
              <td>{l.reason}</td>
              <td>{l.startDate}</td>
              <td>{l.status}</td>
              <td>
                {l.status === "PENDING" && (
                  <button onClick={() => approve(l.id)}>Approve</button>
                )}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

export default LeaveList;