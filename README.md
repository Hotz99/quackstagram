# **Quackstagram:** 

**Introduction**

Quackstagram is a minimalist version of Instagram designed for effortless image sharing and social interaction. Introduced as a project of the Computer Science bachelor programme at Maastricht University. Built using Java, it focuses on providing users with a straightforward and engaging platform to share snapshots of their lives, connect with others, and explore new content. 

The entry point for this program is App.java, located at "src/app/App.java". The database connection credentials are located at "src/database/DatabaseHandler.java".


**Features**

User Profiles: Sign up, sign in, and personalize your profile with photos and captions.
Image Sharing: Upload images with captions to share moments of your life.
Social Interaction: Like images, follow users, and explore new posts to connect with the community.
Notifications: Stay updated with alerts on interactions with your posts.
Design and Architecture

Quackstagram adheres to core software design principles and patterns to ensure a robust, maintainable, and user-friendly application. 

**Design Patterns and Principles**

DRY (Don't Repeat Yourself): We've minimized repetition across the codebase to enhance maintainability and scalability.
SOLID Principles: Each class is designed with a single responsibility, open for extension but closed for modification, and dependency inversion among others to ensure a decoupled and easily adaptable architecture.
GRASP (General Responsibility Assignment Software Patterns): Used to ensure a proper responsibility assignment, enhancing readability and the overall quality of the code.
Class and Method Design: Each method performs a single functionality, and classes are focused on a single concept, ensuring clarity and ease of debugging.
Separation of Concerns: UI and business logic, including credential handling, are kept separate to enhance security and make the codebase more organized.

**License**

Quackstagram is open-sourced under the MIT license. Feel free to fork, modify, and use it in your projects. We are not affiliated with Maastricht University.

2024