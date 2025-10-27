// Background service worker for Student Buddy extension

chrome.runtime.onInstalled.addListener(() => {
    console.log('Student Buddy extension installed');
});

// Handle messages from content script and popup
chrome.runtime.onMessage.addListener((request, sender, sendResponse) => {
    switch (request.action) {
        case 'openPopup':
            chrome.action.openPopup();
            break;
            
        case 'quickAction':
            handleQuickAction(request, sendResponse);
            break;
            
        case 'getProblemInfo':
            // Forward to content script
            chrome.tabs.query({active: true, currentWindow: true}, (tabs) => {
                chrome.tabs.sendMessage(tabs[0].id, request, sendResponse);
            });
            return true; // Keep message channel open for async response
            
        default:
            console.log('Unknown action:', request.action);
    }
});

async function handleQuickAction(request, sendResponse) {
    try {
        const { type, problemSlug } = request;
        
        // Get stored authentication data
        const result = await chrome.storage.local.get(['token', 'username']);
        
        if (!result.token || !result.username) {
            sendResponse({ success: false, message: 'Please login first' });
            return;
        }

        const apiBaseUrl = 'http://localhost:8080/api';
        const headers = {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${result.token}`
        };

        let response;
        
        switch (type) {
            case 'hints':
                response = await fetch(`${apiBaseUrl}/hints/${problemSlug}`, { headers });
                if (response.ok) {
                    const data = await response.json();
                    showNotification('Hints', data.hints || 'No hints available');
                } else {
                    showNotification('Error', 'Failed to load hints');
                }
                break;
                
            case 'notes':
                response = await fetch(`${apiBaseUrl}/notes/${problemSlug}/${result.username}`, { headers });
                if (response.ok) {
                    const data = await response.json();
                    showNotification('Notes', data.notes || 'No notes available');
                } else {
                    showNotification('Error', 'Failed to load notes');
                }
                break;
                
            case 'template':
                // Load default template for quick access
                const templates = {
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
                
                const language = 'java'; // Default to Java
                showNotification('Template', templates[language]);
                break;
        }
        
        sendResponse({ success: true });
    } catch (error) {
        console.error('Quick action error:', error);
        sendResponse({ success: false, message: 'Network error' });
    }
}

function showNotification(title, message) {
    // Create a temporary notification element
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        background: white;
        padding: 15px;
        border-radius: 8px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
        z-index: 10001;
        max-width: 300px;
        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
        border-left: 4px solid #4299e1;
    `;
    
    notification.innerHTML = `
        <h4 style="margin: 0 0 8px 0; color: #333; font-size: 14px;">${title}</h4>
        <p style="margin: 0; color: #666; font-size: 12px; white-space: pre-wrap;">${message}</p>
        <button onclick="this.parentElement.remove()" style="
            position: absolute;
            top: 5px;
            right: 5px;
            background: none;
            border: none;
            font-size: 16px;
            cursor: pointer;
            color: #999;
        ">×</button>
    `;
    
    document.body.appendChild(notification);
    
    // Auto remove after 5 seconds
    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 5000);
}

// Handle extension icon click
chrome.action.onClicked.addListener((tab) => {
    chrome.action.openPopup();
});
