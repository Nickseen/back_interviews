package hr.recruitment.config;

import hr.recruitment.model.User;
import hr.recruitment.model.enums.Role;
import hr.recruitment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        long existingUsers = userRepository.count();
        if (existingUsers < 50) {
            log.info("Database contains {} users. Populating with sample data to reach 50 users...", existingUsers);
            createSampleUsers();
            log.info("Sample data created successfully! Total users: {}", userRepository.count());
        } else {
            log.info("Database already contains {} users. Skipping data initialization.", existingUsers);
        }
    }

    private void createSampleUsers() {
        List<User> users = new ArrayList<>();
        Random random = new Random();

        // Sample names and domains for realistic email generation
        String[] firstNames = {
            "John", "Jane", "Michael", "Sarah", "David", "Emily", "Robert", "Jessica", 
            "William", "Ashley", "James", "Amanda", "Christopher", "Stephanie", "Daniel", 
            "Jennifer", "Matthew", "Elizabeth", "Anthony", "Heather", "Mark", "Nicole", 
            "Donald", "Samantha", "Steven", "Rachel", "Paul", "Amy", "Andrew", "Angela",
            "Joshua", "Brenda", "Kenneth", "Emma", "Kevin", "Olivia", "Brian", "Cynthia",
            "George", "Marie", "Edward", "Janet", "Ronald", "Catherine", "Timothy", "Frances",
            "Jason", "Christine", "Jeffrey", "Deborah"
        };

        String[] lastNames = {
            "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
            "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
            "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson",
            "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker",
            "Young", "Allen", "King", "Wright", "Scott", "Torres", "Nguyen", "Hill",
            "Flores", "Green", "Adams", "Nelson", "Baker", "Hall", "Rivera", "Campbell",
            "Mitchell", "Carter"
        };

        String[] domains = {
            "gmail.com", "yahoo.com", "hotmail.com", "outlook.com", "company.com", 
            "email.com", "mail.com", "example.com", "test.com", "domain.com"
        };

        String[] jobTitles = {
            "Software Developer", "Senior Java Developer", "Full Stack Developer", 
            "Frontend Developer", "Backend Developer", "DevOps Engineer", "Data Scientist",
            "Product Manager", "UI/UX Designer", "Quality Assurance Engineer", 
            "System Administrator", "Database Administrator", "Business Analyst",
            "Project Manager", "Technical Lead", "Software Architect", "Mobile Developer",
            "Cloud Engineer", "Security Engineer", "Machine Learning Engineer"
        };

        String[] cvTemplates = {
            "Experienced professional with 5+ years in software development. Skilled in Java, Spring Boot, and microservices architecture.",
            "Passionate developer with expertise in modern web technologies. Strong background in React, Node.js, and cloud platforms.",
            "Senior engineer with proven track record in enterprise applications. Specializes in system design and team leadership.",
            "Full-stack developer with experience in both frontend and backend technologies. Committed to clean code and best practices.",
            "Results-driven professional with strong analytical skills. Experience in agile methodologies and continuous integration.",
            "Creative problem solver with expertise in user experience design. Strong collaboration and communication skills.",
            "Detail-oriented engineer with focus on performance optimization and scalability. Experience with distributed systems.",
            "Innovative developer passionate about emerging technologies. Strong foundation in computer science fundamentals.",
            "Experienced team player with leadership capabilities. Proven ability to deliver complex projects on time.",
            "Versatile professional with broad technical skills. Adaptable to new technologies and changing requirements."
        };

        // Create 30 candidates and 20 recruiters
        long startId = userRepository.count() + 1;
        for (int i = (int)startId; i < startId + 50; i++) {
            User user = new User();
            
            String firstName = firstNames[random.nextInt(firstNames.length)];
            String lastName = lastNames[random.nextInt(lastNames.length)];
            String domain = domains[random.nextInt(domains.length)];
            
            user.setName(firstName + " " + lastName);
            user.setEmail(firstName.toLowerCase() + lastName.toLowerCase() + i + "@" + domain);
            
            // First 30 users are candidates, rest are recruiters
            if (i - startId + 1 <= 30) {
                user.setRole(Role.ROLE_CANDIDATE);
                user.setInfo(jobTitles[random.nextInt(jobTitles.length)]);
                user.setCv(cvTemplates[random.nextInt(cvTemplates.length)]);
            } else if (i - startId + 1 <= 48) {
                user.setRole(Role.ROLE_RECRUITER);
                user.setInfo("HR Professional with " + (random.nextInt(10) + 1) + " years of experience in talent acquisition.");
                // CV is null for recruiters
            } else {
                // Last 2 users are admins
                user.setRole(Role.ROLE_ADMIN);
                user.setInfo("System Administrator with full access privileges.");
                // CV is null for admins
            }
            
            users.add(user);
        }

        userRepository.saveAll(users);
        log.info("Created {} users: {} candidates, {} recruiters, {} admins", 
                users.size(), 
                users.stream().mapToInt(u -> u.getRole() == Role.ROLE_CANDIDATE ? 1 : 0).sum(),
                users.stream().mapToInt(u -> u.getRole() == Role.ROLE_RECRUITER ? 1 : 0).sum(),
                users.stream().mapToInt(u -> u.getRole() == Role.ROLE_ADMIN ? 1 : 0).sum());
    }
}