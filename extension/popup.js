class StudentBuddyPopup {
    constructor() {
        this.apiBaseUrl = 'http://localhost:8081/api';
        this.token = null;
        this.currentUser = null;
        this.init();
    }

    async init() {
        await this.loadStoredData();
        this.setupEventListeners();
        this.updateUI();
    }

    async loadStoredData() {
        try {
            const result = await chrome.storage.local.get(['token', 'username']);
            this.token = result.token;
            this.currentUser = result.username;
        } catch (error) {
            console.error('Error loading stored data:', error);
        }
    }

    setupEventListeners() {
        // Auth buttons
        document.getElementById('loginBtn').addEventListener('click', () => this.showAuthForm('login'));
        document.getElementById('registerBtn').addEventListener('click', () => this.showAuthForm('register'));
        document.getElementById('logoutBtn').addEventListener('click', () => this.logout());
        document.getElementById('cancelAuth').addEventListener('click', () => this.hideAuthForm());

        // Auth form
        document.getElementById('submitAuth').addEventListener('click', () => this.handleAuth());

        // Dashboard actions
        document.getElementById('updateStreak').addEventListener('click', () => this.updateStreak());
        document.getElementById('loadTemplate').addEventListener('click', () => this.loadTemplate());
        document.getElementById('loadNotes').addEventListener('click', () => this.loadNotes());
        document.getElementById('saveNotes').addEventListener('click', () => this.saveNotes());
        document.getElementById('shareNotes').addEventListener('click', () => this.shareNotes());
        document.getElementById('loadHints').addEventListener('click', () => this.loadHints());
    }

    updateUI() {
        const authSection = document.getElementById('authSection');
        const userSection = document.getElementById('userSection');
        const authForm = document.getElementById('authForm');
        const dashboard = document.getElementById('dashboard');

        if (this.currentUser) {
            authSection.style.display = 'none';
            userSection.style.display = 'flex';
            authForm.style.display = 'none';
            dashboard.style.display = 'block';
            document.getElementById('username').textContent = this.currentUser;
            this.loadUserData();
        } else {
            authSection.style.display = 'flex';
            userSection.style.display = 'none';
            authForm.style.display = 'none';
            dashboard.style.display = 'none';
        }
    }

    showAuthForm(type) {
        document.getElementById('authForm').style.display = 'block';
        document.getElementById('submitAuth').textContent = type === 'login' ? 'Login' : 'Register';
        document.getElementById('submitAuth').dataset.type = type;
    }

    hideAuthForm() {
        document.getElementById('authForm').style.display = 'none';
        document.getElementById('authUsername').value = '';
        document.getElementById('authPassword').value = '';
    }

    async handleAuth() {
        const username = document.getElementById('authUsername').value;
        const password = document.getElementById('authPassword').value;
        const type = document.getElementById('submitAuth').dataset.type;

        if (!username || !password) {
            alert('Please fill in all fields');
            return;
        }

        try {
            const response = await fetch(`${this.apiBaseUrl}/auth/${type}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password })
            });

            const data = await response.json();

            if (response.ok) {
                this.token = data.token;
                this.currentUser = username;
                await chrome.storage.local.set({ token: this.token, username: this.currentUser });
                this.hideAuthForm();
                this.updateUI();
            } else {
                alert(data.message || 'Authentication failed');
            }
        } catch (error) {
            console.error('Auth error:', error);
            alert('Network error. Please check if backend is running.');
        }
    }

    async logout() {
        this.token = null;
        this.currentUser = null;
        await chrome.storage.local.remove(['token', 'username']);
        this.updateUI();
    }

    async loadUserData() {
        if (this.currentUser) {
            await this.loadStreak();
        }
    }

    async loadStreak() {
        try {
            const response = await this.makeAuthenticatedRequest(`/streak/${this.currentUser}`);
            if (response.ok) {
                const data = await response.json();
                document.getElementById('currentStreak').textContent = data.currentStreak || 0;
                const progress = Math.min((data.currentStreak || 0) * 10, 100);
                document.getElementById('streakProgress').style.width = `${progress}%`;
            }
        } catch (error) {
            console.error('Error loading streak:', error);
        }
    }

    async updateStreak() {
        try {
            const response = await this.makeAuthenticatedRequest('/streak/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username: this.currentUser })
            });

            if (response.ok) {
                await this.loadStreak();
                alert('Streak updated! Great job! 🎉');
            } else {
                alert('Failed to update streak');
            }
        } catch (error) {
            console.error('Error updating streak:', error);
            alert('Error updating streak');
        }
    }

    async loadTemplate() {
        const language = document.getElementById('templateLanguage').value;
        try {
            const response = await this.makeAuthenticatedRequest(`/templates/${language}`);
            if (response.ok) {
                const data = await response.json();
                document.getElementById('templatePreview').textContent = data.template || 'No template available';
            } else {
                // Fallback to default templates
                const defaultTemplates = this.getDefaultTemplates();
                document.getElementById('templatePreview').textContent = defaultTemplates[language] || 'No template available';
            }
        } catch (error) {
            console.error('Error loading template:', error);
            const defaultTemplates = this.getDefaultTemplates();
            document.getElementById('templatePreview').textContent = defaultTemplates[language] || 'No template available';
        }
    }

    getDefaultTemplates() {
        return {
            java: `public class Solution {
    public int[] twoSum(int[] nums, int target) {
        // Your code here
        return new int[0];
    }
}`,
            python: `class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        # Your code here
        return []`,
            cpp: `class Solution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        // Your code here
        return {};
    }
};`
        };
    }

    async loadNotes() {
        const slug = document.getElementById('problemSlug').value;
        if (!slug) {
            alert('Please enter a problem slug');
            return;
        }

        try {
            const response = await this.makeAuthenticatedRequest(`/notes/${slug}/${this.currentUser}`);
            if (response.ok) {
                const data = await response.json();
                document.getElementById('notesTextarea').value = data.notes || '';
            } else {
                document.getElementById('notesTextarea').value = '';
            }
        } catch (error) {
            console.error('Error loading notes:', error);
            document.getElementById('notesTextarea').value = '';
        }
    }

    async saveNotes() {
        const slug = document.getElementById('problemSlug').value;
        const notes = document.getElementById('notesTextarea').value;

        if (!slug) {
            alert('Please enter a problem slug');
            return;
        }

        try {
            const response = await this.makeAuthenticatedRequest('/notes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    slug,
                    username: this.currentUser,
                    notes
                })
            });

            if (response.ok) {
                alert('Notes saved successfully!');
            } else {
                alert('Failed to save notes');
            }
        } catch (error) {
            console.error('Error saving notes:', error);
            alert('Error saving notes');
        }
    }

    async shareNotes() {
        const slug = document.getElementById('problemSlug').value;
        const notes = document.getElementById('notesTextarea').value;

        if (!slug || !notes) {
            alert('Please enter problem slug and notes to share');
            return;
        }

        const friendUsername = prompt('Enter friend\'s username:');
        if (!friendUsername) return;

        try {
            const response = await this.makeAuthenticatedRequest('/notes/share', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    slug,
                    fromUsername: this.currentUser,
                    toUsername: friendUsername,
                    notes
                })
            });

            if (response.ok) {
                alert('Notes shared successfully!');
            } else {
                alert('Failed to share notes');
            }
        } catch (error) {
            console.error('Error sharing notes:', error);
            alert('Error sharing notes');
        }
    }

    async loadHints() {
        const slug = document.getElementById('hintSlug').value;
        if (!slug) {
            alert('Please enter a problem slug');
            return;
        }

        try {
            // Try LeetCode GraphQL API first
            const leetcodeResponse = await this.fetchLeetCodeHints(slug);
            if (leetcodeResponse) {
                document.getElementById('hintsContent').textContent = leetcodeResponse;
                return;
            }

            // Fallback to backend
            const response = await this.makeAuthenticatedRequest(`/hints/${slug}`);
            if (response.ok) {
                const data = await response.json();
                document.getElementById('hintsContent').textContent = data.hints || 'No hints available';
            } else {
                document.getElementById('hintsContent').textContent = 'No hints available';
            }
        } catch (error) {
            console.error('Error loading hints:', error);
            document.getElementById('hintsContent').textContent = 'Error loading hints';
        }
    }

    async fetchLeetCodeHints(slug) {
        try {
            const query = `
                query getQuestionDetail($titleSlug: String!) {
                    question(titleSlug: $titleSlug) {
                        hints
                        difficulty
                        title
                    }
                }
            `;

            const response = await fetch('https://leetcode.com/graphql', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    query,
                    variables: { titleSlug: slug }
                })
            });

            const data = await response.json();
            if (data.data && data.data.question && data.data.question.hints) {
                return data.data.question.hints.join('\n\n');
            }
            return null;
        } catch (error) {
            console.error('LeetCode API error:', error);
            return null;
        }
    }

    async makeAuthenticatedRequest(endpoint, options = {}) {
        const url = `${this.apiBaseUrl}${endpoint}`;
        const headers = {
            'Content-Type': 'application/json',
            ...options.headers
        };

        if (this.token) {
            headers['Authorization'] = `Bearer ${this.token}`;
        }

        return fetch(url, {
            ...options,
            headers
        });
    }
}

// Initialize the popup when DOM is loaded
document.addEventListener('DOMContentLoaded', () => {
    new StudentBuddyPopup();
});
