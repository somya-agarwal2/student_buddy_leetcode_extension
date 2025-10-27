-- Default code templates
INSERT INTO code_templates (language, template_name, template_content, is_default, created_at, updated_at) VALUES
('java', 'Default Java Template', 'public class Solution { public int[] twoSum(int[] nums, int target) { return new int[0]; } }', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO code_templates (language, template_name, template_content, is_default, created_at, updated_at) VALUES
('python', 'Default Python Template', 'class Solution:\n    def twoSum(self, nums: List[int], target: int) -> List[int]:\n        return []', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO code_templates (language, template_name, template_content, is_default, created_at, updated_at) VALUES
('cpp', 'Default C++ Template', 'class Solution { public: vector<int> twoSum(vector<int>& nums, int target) { return {}; } };', TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Sample problem hints
INSERT INTO problem_hints (problem_slug, hints, difficulty, title, created_at, updated_at) VALUES
('Two Sum', 'Use a hash map to store numbers and their indices. For each number, check if target - current number exists in the map.', 'Easy', 'Two Sum', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO problem_hints (problem_slug, hints, difficulty, title, created_at, updated_at) VALUES
('add-two-numbers', 'Use a dummy head node and carry variable. Traverse both lists simultaneously and handle different lengths.', 'Medium', 'Add Two Numbers', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO problem_hints (problem_slug, hints, difficulty, title, created_at, updated_at) VALUES
('longest-substring-without-repeating-characters', 'Use sliding window technique with two pointers. Keep track of characters in current window using a set.', 'Medium', 'Longest Substring Without Repeating Characters', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
