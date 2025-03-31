// Function to initialize the history functionality
function initMessageHistory(contextPath) {
    document.addEventListener('DOMContentLoaded', function() {
        const showHistoryBtn = document.getElementById('showHistoryBtn');
        const historyTable = document.getElementById('historyTable');
        const historyTableBody = document.getElementById('historyTableBody');

        showHistoryBtn.addEventListener('click', function() {
            const messageId = document.getElementById('messageId').value;
            const url = contextPath + '/history/messages/' + messageId;

            fetch(url, {
                method: 'GET'
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    historyTableBody.innerHTML = '';

                    if (data && data.length > 0) {
                        data.forEach(message => {
                            const row = document.createElement('tr');
                            row.innerHTML = `
                                <td>${message.historyMessageId}</td>
                                <td>${message.content}</td>
                                <td>${message.historyCreationDate}</td>
                                <td>${message.updateDate}</td>
                            `;
                            historyTableBody.appendChild(row);
                        });
                    } else {
                        const row = document.createElement('tr');
                        row.innerHTML = '<td colspan="4">No history found for this message</td>';
                        historyTableBody.appendChild(row);
                    }
                    historyTable.style.display = 'table';
                })
                .catch(error => {
                    historyTableBody.innerHTML = '';
                    const row = document.createElement('tr');
                    row.innerHTML = `<td colspan="4">Error loading history: ${error.message}</td>`;
                    historyTableBody.appendChild(row);
                    historyTable.style.display = 'table';
                });
        });
    });
}