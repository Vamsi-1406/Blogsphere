# Blogsphere Application Report

## Application Overview

Blogsphere is a blogging platform that allows users to create, read, update, and delete blog posts, as well as interact with other users through comments and likes.

## Database Schema

The application uses the following entity model:

- **User**: Represents a user of the platform
  - Fields: id, username, email, password, fullName, bio, profilePicture, createdAt, updatedAt
  - Relationships: posts, followers, following, likedPosts

- **Post**: Represents a blog post
  - Fields: id, title, content, createdAt, updatedAt
  - Relationships: author, comments, tags, likedBy

- **Comment**: Represents a comment on a blog post
  - Fields: id, content, createdAt, updatedAt
  - Relationships: post, author

- **Tag**: Represents a categorization tag for blog posts
  - Fields: id, name
  - Relationships: posts

## API Endpoints

### Authentication

- `GET /login` - Show login page
- `POST /login` - Process login
- `GET /register` - Show registration form
- `POST /register` - Process user registration
- `POST /logout` - Log out user

### Posts

- `GET /posts` - List all posts with pagination
- `GET /posts/new` - Show form to create a new post
- `POST /posts/new` - Process post creation
- `GET /posts/{id}` - View a specific post
- `GET /posts/{id}/edit` - Show form to edit a post
- `POST /posts/{id}/edit` - Process post update
- `POST /posts/{id}/delete` - Delete a post
- `POST /posts/{id}/like` - Like a post
- `POST /posts/{id}/unlike` - Unlike a post

### Comments

- `POST /posts/{postId}/comments` - Add a comment to a post
- `GET /posts/{postId}/comments/{commentId}/edit` - Show form to edit a comment
- `POST /posts/{postId}/comments/{commentId}/edit` - Process comment update
- `POST /posts/{postId}/comments/{commentId}/delete` - Delete a comment
- `GET /posts/{postId}/comments` - Get paginated comments for a post

### Users

- `GET /users/{id}` - View a user's profile
- `GET /profile` - View own profile
- `GET /profile/edit` - Show form to edit own profile
- `POST /profile/edit` - Process profile update
- `POST /users/{id}/follow` - Follow a user
- `POST /users/{id}/unfollow` - Unfollow a user

## Templates

### Authentication
- `login.html` - Login form
- `register.html` - Registration form

### Layout
- `layout/main.html` - Main layout template

### Posts
- `posts/list.html` - List of posts with pagination
- `posts/create.html` - Form for creating a new post
- `posts/view.html` - View of a specific post with comments
- `posts/edit.html` - Form for editing a post

### Comments
- `comments/edit.html` - Form for editing a comment

### User
- `profile.html` - User profile view
- `edit-profile.html` - Form for editing a profile

## Testing

All components have been successfully tested and are functioning properly:
- Entity models for User, Post, Comment, and Tag
- Repository layer with proper data access
- Service layer with business logic
- Controller layer with endpoint handling
- Frontend templates for user interaction

The application is configured to use an H2 in-memory database for testing purposes but can be readily switched to use MySQL for production by modifying the application.properties file.

## Security

Security has been implemented using Spring Security:
- Authentication and authorization for all routes
- Password encryption using BCrypt
- Protection against CSRF attacks
- Session management

## Deployment

The application can be deployed by packaging it into a JAR file using `mvn package` and then running the JAR file using `java -jar blogsphere-0.0.1-SNAPSHOT.jar`. 