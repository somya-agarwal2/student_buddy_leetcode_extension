// Content script for LeetCode integration
class LeetCodeIntegration {
    constructor() {
        this.init();
    }

    init() {
        this.createFloatingButton();
        this.setupEventListeners();
    }

    createFloatingButton() {
        // Create floating action button
        const button = document.createElement('div');
        button.id = 'student-buddy-fab';
        button.innerHTML = '🎓';
        button.title = 'Student Buddy';
        
        // Style the button
        Object.assign(button.style, {
            position: 'fixed',
            bottom: '20px',
            right: '20px',
            width: '50px',
            height: '50px',
            borderRadius: '50%',
            backgroundColor: '#4299e1',
            color: 'white',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            fontSize: '20px',
            cursor: 'pointer',
            zIndex: '10000',
            boxShadow: '0 4px 15px rgba(0, 0, 0, 0.2)',
            transition: 'all 0.3s ease',
            border: 'none'
        });

        // Hover effects
        button.addEventListener('mouseenter', () => {
            button.style.transform = 'scale(1.1)';
            button.style.backgroundColor = '#3182ce';
        });

        button.addEventListener('mouseleave', () => {
            button.style.transform = 'scale(1)';
            button.style.backgroundColor = '#4299e1';
        });

        document.body.appendChild(button);
    }

    setupEventListeners() {
        // Listen for clicks on the floating button
        document.addEventListener('click', (e) => {
            if (e.target.id === 'student-buddy-fab') {
                this.openStudentBuddyPopup();
            }
        });

        // Listen for messages from popup
        chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
            if (request.action === 'getProblemInfo') {
                const problemInfo = this.extractProblemInfo();
                sendResponse(problemInfo);
            }
        });
    }

    openStudentBuddyPopup() {
        // Open the extension popup
        chrome.runtime.sendMessage({ action: 'openPopup' });
    }

    extractProblemInfo() {
        // Extract problem information from LeetCode page
        const problemInfo = {
            slug: this.getProblemSlug(),
            title: this.getProblemTitle(),
            difficulty: this.getProblemDifficulty(),
            url: window.location.href
        };

        return problemInfo;
    }

    getProblemSlug() {
        // Extract problem slug from URL
        const pathMatch = window.location.pathname.match(/\/problems\/([^\/]+)/);
        return pathMatch ? pathMatch[1] : null;
    }

    getProblemTitle() {
        // Extract problem title from page
        const titleElement = document.querySelector('[data-cy="question-title"]') || 
                           document.querySelector('h1') ||
                           document.querySelector('.title');
        return titleElement ? titleElement.textContent.trim() : null;
    }

    getProblemDifficulty() {
        // Extract difficulty from page
        const difficultyElement = document.querySelector('[diff]') ||
                                document.querySelector('.difficulty') ||
                                document.querySelector('[data-cy="difficulty"]');
        
        if (difficultyElement) {
            const diff = difficultyElement.textContent.trim().toLowerCase();
            return diff.includes('easy') ? 'Easy' : 
                   diff.includes('medium') ? 'Medium' : 
                   diff.includes('hard') ? 'Hard' : null;
        }
        return null;
    }

    // Inject helper UI elements into LeetCode page
    injectHelperUI() {
        // Add a small helper panel for quick access
        const helperPanel = document.createElement('div');
        helperPanel.id = 'student-buddy-helper';
        helperPanel.innerHTML = `
            <div style="background: white; padding: 10px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); margin: 10px;">
                <h4 style="margin: 0 0 10px 0; color: #333;">Student Buddy</h4>
                <button id="quick-hints" style="margin: 2px; padding: 5px 10px; background: #4299e1; color: white; border: none; border-radius: 4px; cursor: pointer;">Get Hints</button>
                <button id="quick-notes" style="margin: 2px; padding: 5px 10px; background: #48bb78; color: white; border: none; border-radius: 4px; cursor: pointer;">Add Notes</button>
                <button id="quick-template" style="margin: 2px; padding: 5px 10px; background: #ed8936; color: white; border: none; border-radius: 4px; cursor: pointer;">Load Template</button>
            </div>
        `;

        // Position the helper panel
        Object.assign(helperPanel.style, {
            position: 'fixed',
            top: '20px',
            right: '20px',
            zIndex: '9999',
            maxWidth: '200px'
        });

        document.body.appendChild(helperPanel);

        // Add event listeners for quick actions
        document.getElementById('quick-hints').addEventListener('click', () => {
            this.quickAction('hints');
        });

        document.getElementById('quick-notes').addEventListener('click', () => {
            this.quickAction('notes');
        });

        document.getElementById('quick-template').addEventListener('click', () => {
            this.quickAction('template');
        });
    }

    quickAction(action) {
        const problemSlug = this.getProblemSlug();
        if (!problemSlug) {
            alert('Could not detect problem slug. Please use the extension popup.');
            return;
        }

        // Send message to background script to handle quick actions
        chrome.runtime.sendMessage({
            action: 'quickAction',
            type: action,
            problemSlug: problemSlug
        });
    }
}

// Initialize when page loads
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', () => {
        new LeetCodeIntegration();
    });
} else {
    new LeetCodeIntegration();
}
