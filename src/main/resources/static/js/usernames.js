// messageBoard.js
$(document).ready(function() {
    $('.username').each(function() {
        const userId = $(this).data('userid');
        const $usernameSpan = $(this);
        console.log('Fetching username for userId:', userId);

        // Use the baseUrl variable defined in the HTML
        $.ajax({
            url: baseUrl + 'rest/users/' + userId, // baseUrl will be injected from HTML
            method: 'GET',
            success: function(username) {
                $usernameSpan.text(username);
            },
            error: function(xhr, status, error) {
                console.log('Error for userId:', userId, 'Status:', xhr.status);
                if (xhr.status === 404) {
                    $usernameSpan.text('User Not Found');
                } else {
                    $usernameSpan.text('Error Loading User');
                }
            }
        });
    });
});