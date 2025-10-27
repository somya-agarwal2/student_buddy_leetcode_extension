# 🎓 Student Buddy - Chrome Extension & Spring Boot Backend

A comprehensive Chrome Extension with Spring Boot backend to help students with coding problems on LeetCode and DSA platforms.

## 🚀 Features

### Chrome Extension (Frontend)
- **Problem Hints & Explanations**: Fetch hints from LeetCode GraphQL API with backend fallback
- **Code Templates**: Starter templates for Java, Python, C++ with custom template saving
- **Daily Streak Tracker**: Track problems solved per day with progress visualization
- **Notes & Sharing**: Personal notes for each problem with friend sharing functionality
- **LeetCode Integration**: Floating action button and content script for seamless integration

### Spring Boot Backend
- **JWT Authentication**: Secure user registration and login
- **REST APIs**: Complete API suite for all extension features
- **Database Integration**: JPA with H2 (dev) and MySQL (prod) support
- **CORS Enabled**: Ready for Chrome Extension integration

## 📁 Project Structure

```
Student_Buddy_Project/
├── extension/                    # Chrome Extension (Manifest V3)
│   ├── manifest.json
│   ├── popup.html
│   ├── popup.css
│   ├── popup.js
│   ├── content.js
│   ├── content.css
│   ├── background.js
│   └── icons/                    # Extension icons (add your own)
├── backend/                      # Spring Boot Backend
│   ├── src/main/java/com/studentbuddy/
│   │   ├── controller/           # REST Controllers
│   │   ├── service/              # Business Logic
│   │   ├── repository/           # Data Access Layer
│   │   ├── model/                # JPA Entities
│   │   ├── dto/                  # Data Transfer Objects
│   │   ├── security/             # JWT & Security Config
│   │   └── StudentBuddyApplication.java
│   ├── src/main/resources/
│   │   ├── application.properties        # H2 Database (Dev)
│   │   └── application-mysql.properties  # MySQL Database (Prod)
│   └── pom.xml
└── README.md
```

## 🛠️ Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Chrome Browser
- MySQL (for production)

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend
   ```

2. **Run with H2 Database (Development):**
   ```bash
   mvn spring-boot:run
   ```
   - Backend will start on `http://localhost:8080`
   - H2 Console available at `http://localhost:8080/h2-console`
   - Database URL: `jdbc:h2:mem:studentbuddy`
   - Username: `sa`, Password: `password`

3. **Run with MySQL (Production):**
   ```bash
   # Create MySQL database
   mysql -u root -p
   CREATE DATABASE studentbuddy;
   
   # Update application-mysql.properties with your MySQL credentials
   # Then run:
   mvn spring-boot:run -Dspring.profiles.active=mysql
   ```

### Chrome Extension Setup

1. **Open Chrome Extensions:**
   - Go to `chrome://extensions/`
   - Enable "Developer mode" (toggle in top-right)

2. **Load Extension:**
   - Click "Load unpacked"
   - Select the `extension` folder from this project

3. **Add Icons (Optional):**
   - Create icons in `extension/icons/` folder:
     - `icon16.png` (16x16)
     - `icon32.png` (32x32)
     - `icon48.png` (48x48)
     - `icon128.png` (128x128)

4. **Test Extension:**
   - Visit any LeetCode problem page
   - Click the Student Buddy extension icon
   - Register/Login and start using features

## 🔧 API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login

### Streak Management
- `GET /api/streak/{username}` - Get user streak
- `POST /api/streak/update` - Update streak

### Notes Management
- `GET /api/notes/{slug}/{username}` - Get problem notes
- `POST /api/notes` - Save/update notes
- `POST /api/notes/share` - Share notes with friends

### Code Templates
- `GET /api/templates/{language}?username={username}` - Get template
- `POST /api/templates` - Save custom template

### Problem Hints
- `GET /api/hints/{slug}` - Get problem hints
- `POST /api/hints` - Save hints

## 🎯 Usage

### Chrome Extension Features

1. **Authentication:**
   - Click extension icon
   - Register new account or login
   - JWT token stored automatically

2. **Daily Streak:**
   - View current streak in popup
   - Click "Mark Problem Solved" to update
   - Progress bar shows streak visualization

3. **Code Templates:**
   - Select language (Java/Python/C++)
   - Click "Load Template" for starter code
   - Templates auto-saved to backend

4. **Problem Notes:**
   - Enter problem slug (e.g., "two-sum")
   - Add personal notes
   - Save and share with friends

5. **Problem Hints:**
   - Enter problem slug
   - Get hints from LeetCode API or backend
   - Fallback system ensures availability

### LeetCode Integration

- **Floating Button:** Appears on LeetCode pages
- **Quick Actions:** Get hints, add notes, load templates
- **Auto-detection:** Extracts problem info from page

## 🔒 Security Features

- JWT-based authentication
- Password encryption with BCrypt
- CORS configuration for extension
- Input validation on all endpoints

## 🗄️ Database Schema

### Tables
- `users` - User accounts
- `problem_notes` - User notes for problems
- `streaks` - Daily streak tracking
- `code_templates` - Code templates
- `problem_hints` - Problem hints cache

## 🚀 Deployment

### Backend Deployment
1. Update `application-mysql.properties` with production database
2. Set environment variable `JWT_SECRET` for production
3. Build and deploy JAR file:
   ```bash
   mvn clean package
   java -jar target/student-buddy-backend-1.0.0.jar
   ```

### Extension Deployment
1. Create production build
2. Zip the `extension` folder
3. Upload to Chrome Web Store (optional)

## 🐛 Troubleshooting

### Common Issues

1. **Extension not loading:**
   - Check Chrome Developer mode is enabled
   - Verify all files are in correct locations
   - Check browser console for errors

2. **Backend connection failed:**
   - Ensure backend is running on port 8080
   - Check CORS configuration
   - Verify JWT token in browser storage

3. **Database connection issues:**
   - Check MySQL credentials
   - Ensure database exists
   - Verify connection string

## 📝 Development

### Adding New Features
1. Create model in `backend/src/main/java/com/studentbuddy/model/`
2. Add repository interface
3. Implement service layer
4. Create REST controller
5. Update extension frontend

### Testing
- Backend: `mvn test`
- Extension: Use Chrome DevTools
- Integration: Test full flow end-to-end

## 🤝 Contributing

1. Fork the repository
2. Create feature branch
3. Make changes
4. Test thoroughly
5. Submit pull request

## 📄 License

This project is open source and available under the MIT License.

## 🆘 Support

For issues and questions:
- Check the troubleshooting section
- Review API documentation
- Create an issue in the repository

---

**Happy Coding! 🎓✨**
