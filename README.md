Key Features
Unique Member Identification: Each group member is assigned a unique ID upon joining. Members provide their ID, server IP address, and port number during the connection process.
Dynamic Coordinator Assignment: The first member to join the group automatically becomes the coordinator. The coordinator's role is dynamic, and in the event the coordinator leaves, another member is seamlessly promoted to the role.
Member State Management: The coordinator periodically checks the status of active group members to maintain an updated list, which can be requested by any member.
Robust Communication: Members can send private or broadcast messages. The system is resilient to member departures, ensuring continued communication among remaining members.
User Input or Automation: The system can be run in two modes: manual, requiring user input for message exchange, or automatic, where clients and servers exchange messages periodically without user intervention.
Fault Tolerance: The system is designed to handle member exits gracefully, promoting a new coordinator if needed and ensuring no disruption in communication.
Design Patterns: Implements key design patterns to ensure scalability, maintainability, and efficient communication.
Testing: Utilizes JUnit for testing the application's components to ensure reliability and correctness.
Version Control: Developed using Git for version control, ensuring efficient collaboration and project management.
This project demonstrates the application of advanced programming principles, including fault tolerance, design patterns, and systematic testing, making it a robust and reliable system for group-based communication in distributed environments.
