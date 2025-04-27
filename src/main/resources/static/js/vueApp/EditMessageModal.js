const EditMessageModal = {
    template: `
        <div v-if="message" class="modal" tabindex="-1" style="display: block;">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Edit Message</h5>
                        <button @click="$emit('close')" type="button" class="btn-close"></button>
                    </div>
                    <div class="modal-body">
                        <form @submit.prevent="updateMessage">
                            <div class="mb-3">
                                <label class="form-label">Message</label>
                                <textarea v-model="editedMessage.content" class="form-control" required minlength="3"></textarea>
                            </div>
                            <button type="submit" class="btn btn-primary" :disabled="isSaving">Save</button>
                            <button @click="$emit('close')" type="button" class="btn btn-secondary" :disabled="isSaving">Cancel</button>
                        </form>
                        <div v-if="errorMessage" class="alert alert-danger mt-3">{{ errorMessage }}</div>
                    </div>
                </div>
            </div>
        </div>
    `,
    props: {
        message: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            editedMessage: null, // Start as null to ensure initialization
            isSaving: false,    // Track saving state
            errorMessage: ''    // Display errors to user
        }
    },
    created() {
        // Initialize editedMessage immediately when component is created
        this.initializeEditedMessage();
    },
    watch: {
        message: {
            handler(newMessage) {
                this.initializeEditedMessage(newMessage);
            },
            deep: true // Watch for deep changes if message is complex
        }
    },
    methods: {
        initializeEditedMessage(newMessage = this.message) {
            if (newMessage) {
                this.editedMessage = { ...newMessage };
            }
        },
        async updateMessage() {
            if (!this.editedMessage || !this.editedMessage.messageId) {
                this.errorMessage = 'Invalid message data';
                return;
            }
            this.isSaving = true;
            this.errorMessage = '';
            try {
                const response = await axios.put(
                    `${window.contextPath}/api/messages/${this.editedMessage.messageId}`,
                    this.editedMessage
                );
                console.log('Update successful:', response.data);
                this.$emit('message-updated');
                this.$emit('close');
            } catch (error) {
                console.error('Error updating message:', error);
                this.errorMessage = error.response?.data?.message || 'Failed to update message';
            } finally {
                this.isSaving = false;
            }
        }
    }
};

Vue.createApp(EditMessageModal).component('edit-message-modal', EditMessageModal);