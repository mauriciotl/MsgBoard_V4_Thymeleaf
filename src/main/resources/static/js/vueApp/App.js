const App = {
    template: `
        <div class="container py-4">
            <message-form @message-added="fetchMessages"></message-form>
            <message-list :messages="messages" @edit-message="openEditModal" @message-deleted="fetchMessages"></message-list>
            <edit-message-modal v-if="editingMessage" :message="editingMessage" 
                @close="editingMessage = null" 
                @message-updated="fetchMessages">
            </edit-message-modal>
        </div>
    `,
    data() {
        return {
            messages: [],
            editingMessage: null
        }
    },
    components: {
        'message-form': MessageForm,
        'message-list': MessageList,
        'edit-message-modal': EditMessageModal
    },
    methods: {
        async fetchMessages() {
            try {
                const response = await axios.get(`${window.contextPath}/api/messages`);
                this.messages = response.data;
            } catch (error) {
                console.error('Error fetching messages:', error);
            }
        },
        openEditModal(message) {
            this.editingMessage = message;
        }
    },
    mounted() {
        this.fetchMessages();
    }
};

Vue.createApp(App).mount('#app');