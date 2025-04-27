const MessageForm = {
    template: `
        <div class="card mb-4">
            <div class="card-body">
                <h5 class="card-title">New Message</h5>
                <form @submit.prevent="submitMessage">
                    <div class="mb-3">
                        <label class="form-label">User ID</label>
                        <input v-model="newMessage.userId" type="number" class="form-control" required>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Message</label>
                        <textarea v-model="newMessage.content" class="form-control" required minlength="3"></textarea>
                    </div>
                    <button type="submit" class="btn btn-primary">Submit</button>
                </form>
            </div>
        </div>
    `,
    data() {
        return {
            newMessage: {
                userId: null,
                content: ''
            }
        }
    },
    methods: {
        async submitMessage() {
            try {
                const response = await axios.post(`${window.contextPath}/api/messages`, this.newMessage);
                this.$emit('message-added');
                this.newMessage = { userId: null, content: '' };
            } catch (error) {
                console.error('Error creating message:', error);
            }
        }
    }
};

Vue.createApp(MessageForm).component('message-form', MessageForm);