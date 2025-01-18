# Real-Time Chat Application

This is a real-time chat application designed to facilitate instant communication between users.

The README will evolve as the development progresses, outlining key features, setup instructions, and more.

## Table of Contents
- [Features](#features)
- [Getting Started](#getting-started)
- [Prerequisites](#Prerequisites)
- [Setup and Installation](#setup-and-installation)
- [Note](#note)

---

## Features

- **Real-Time Messaging**: Instant communication between users in various chat rooms.
- **User Authentication**: Secure login system via JWT tokens and OAuth2 (OAuth2 implementation is pending).
- **Private and Group Chats**: Users can create private chats or join public group chats.
- **Message History**: Messages are stored and can be accessed even after being sent.
- **Typing Indicators**: Shows when a user is typing a message.
- **Notifications**: Users will receive notifications for new messages or user activities.

---

## Getting Started

### Prerequisites

Before you begin, ensure you have the following installed on your system:

- **Java 21** or higher: Required for running the application.
- **Maven**: A build automation tool used for dependency management and running the project.

## Setup and Installation

- Clone the repository: `git clone https://github.com/tpoda35/JAVA.STUDY.rt-chatApp.git`
- Rename the applicationExample.yml to application.yml and fill the data (database, ...).
- Build the project: mvn clean install
- Run the project: mvn spring-boot:run
- The project will run on http://localhost:8080/ by default.

# Note
- **Security System**: Please note that the security 
system (JWT authentication) is not of my own design. 
It leverages existing open-source security solutions and best 
practices.
