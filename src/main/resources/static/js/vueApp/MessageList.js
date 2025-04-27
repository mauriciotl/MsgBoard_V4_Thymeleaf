const MessageList = {
    template: `
        <div class="card">
            <div class="card-body">
                <h5 class="card-title">Messages</h5>
                <div v-if="messages.length === 0" class="alert alert-info">
                    No messages found
                </div>
                <ul class="list-group">
                    <li v-for="message in messages" :key="message.messageId" class="list-group-item">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                <strong>User {{ message.userId }}:</strong> {{ message.content }}
                                <small class="text-muted d-block">{{ formatDate(message.creationDate) }}</small>
                            </div>
                            <div>
                                <button @click="$emit('edit-message', message)" class="btn btn-sm btn-primary me-2">Edit</button>
                                <button @click="deleteMessage(message.messageId)" class="btn btn-sm btn-danger">Delete</button>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>
        </div>
    `,
    props: ['messages'],
    methods: {
        async deleteMessage(messageId) {
            if (confirm('Are you sure you want to delete this message?')) {
                try {
                    await axios.delete(`${window.contextPath}/api/messages/${messageId}`);
                    this.$emit('message-deleted');
                } catch (error) {
                    console.error('Error deleting message:', error);
                }
            }
        },
        formatDate(date) {
            return new Date(date).toLocaleString();
        }
    }
};

Vue.createApp(MessageList).component('message-list', MessageList);