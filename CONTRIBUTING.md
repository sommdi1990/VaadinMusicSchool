# 🤝 Contributing to Music School Management System

Thank you for your interest in contributing to the Music School Management System! This document provides guidelines and information for contributors.

## 📋 Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Setup](#development-setup)
- [Contributing Guidelines](#contributing-guidelines)
- [Pull Request Process](#pull-request-process)
- [Code Standards](#code-standards)
- [Testing](#testing)
- [Documentation](#documentation)
- [Issue Reporting](#issue-reporting)

## 📜 Code of Conduct

### Our Pledge
We are committed to providing a welcoming and inspiring community for all. We expect all contributors to:

- **Be Respectful**: Treat everyone with respect and kindness
- **Be Inclusive**: Welcome people of all backgrounds and experience levels
- **Be Collaborative**: Work together to build something great
- **Be Professional**: Maintain a professional and constructive attitude

### Our Standards
- Use welcoming and inclusive language
- Be respectful of differing viewpoints and experiences
- Accept constructive criticism gracefully
- Focus on what's best for the community
- Show empathy towards other community members

## 🚀 Getting Started

### Prerequisites
- **Java 21**: Latest LTS version
- **Maven 3.9+**: Build management
- **Docker**: Containerization (optional)
- **Git**: Version control
- **IDE**: IntelliJ IDEA, VS Code, or Eclipse

### Fork and Clone
```bash
# Fork the repository on GitHub
# Then clone your fork
git clone https://github.com/your-username/Vaadin.git
cd Vaadin

# Add upstream remote
git remote add upstream https://github.com/original-repo/Vaadin.git
```

### Development Setup
```bash
# Set Java 21
export JAVA_HOME=/home/soroush/.jdks/graalvm-jdk-21.0.8
export PATH=$JAVA_HOME/bin:$PATH

# Verify setup
java -version
mvn -version

# Build the project
./build.sh
```

## 🔧 Development Setup

### IDE Configuration

#### IntelliJ IDEA
1. **Import Project**: Open the project in IntelliJ IDEA
2. **Maven Import**: Let IntelliJ import Maven dependencies
3. **Java 21**: Set project SDK to Java 21
4. **Code Style**: Use the project's code style settings

#### VS Code
1. **Extensions**: Install Java Extension Pack
2. **Java 21**: Set `java.home` in settings
3. **Maven**: Configure Maven settings
4. **Spring Boot**: Install Spring Boot extension

#### Eclipse
1. **Import Project**: Import as Maven project
2. **Java 21**: Set project JRE to Java 21
3. **Maven**: Refresh Maven dependencies
4. **Spring Boot**: Install Spring Boot tools

### Database Setup
```bash
# Start PostgreSQL for development
docker run -d \
  --name postgres \
  -p 5432:5432 \
  -e POSTGRES_DB=musicschool \
  -e POSTGRES_USER=musicschool \
  -e POSTGRES_PASSWORD=musicschool123 \
  postgres:15-alpine
```

### Running the Application
```bash
# Quick development build
./quick-build.sh

# Run with Maven
mvn spring-boot:run

# Or run JAR file
java -jar target/music-school-management-1.0.0.jar
```

## 📝 Contributing Guidelines

### Types of Contributions
- **Bug Fixes**: Fix existing issues
- **Feature Additions**: Add new functionality
- **Documentation**: Improve documentation
- **Testing**: Add or improve tests
- **Performance**: Optimize performance
- **Security**: Improve security

### Contribution Process
1. **Check Issues**: Look for existing issues or create new ones
2. **Fork Repository**: Fork the repository on GitHub
3. **Create Branch**: Create a feature branch from `main`
4. **Make Changes**: Implement your changes
5. **Test Changes**: Ensure all tests pass
6. **Update Documentation**: Update relevant documentation
7. **Submit PR**: Create a pull request

### Branch Naming
- **Feature**: `feature/description` (e.g., `feature/student-search`)
- **Bug Fix**: `bugfix/description` (e.g., `bugfix/login-issue`)
- **Documentation**: `docs/description` (e.g., `docs/api-update`)
- **Refactoring**: `refactor/description` (e.g., `refactor/service-layer`)

## 🔄 Pull Request Process

### Before Submitting
- [ ] **Code Quality**: Ensure code follows project standards
- [ ] **Tests**: Add tests for new functionality
- [ ] **Documentation**: Update relevant documentation
- [ ] **Build**: Ensure build passes without errors
- [ ] **Linting**: Fix any linting issues

### PR Template
```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Documentation update
- [ ] Performance improvement
- [ ] Security enhancement

## Testing
- [ ] Unit tests added/updated
- [ ] Integration tests added/updated
- [ ] Manual testing completed

## Documentation
- [ ] README updated
- [ ] API documentation updated
- [ ] Code comments added
- [ ] Changelog updated

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Tests pass locally
- [ ] Documentation updated
- [ ] No breaking changes (or documented)
```

### Review Process
1. **Automated Checks**: CI/CD pipeline runs automatically
2. **Code Review**: Maintainers review the code
3. **Testing**: Ensure all tests pass
4. **Documentation**: Verify documentation is updated
5. **Approval**: Maintainers approve the PR
6. **Merge**: PR is merged into main branch

## 📏 Code Standards

### Java Code Style
```java
// Use meaningful names
public class StudentService {
    private final StudentRepository studentRepository;
    
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    
    public List<Student> findByNameContaining(String name) {
        return studentRepository.findByNameContaining(name);
    }
}
```

### Naming Conventions
- **Classes**: PascalCase (e.g., `StudentService`)
- **Methods**: camelCase (e.g., `findByNameContaining`)
- **Variables**: camelCase (e.g., `studentRepository`)
- **Constants**: UPPER_SNAKE_CASE (e.g., `MAX_STUDENTS`)

### Code Organization
- **Packages**: Organize by feature (e.g., `com.musicschool.student`)
- **Classes**: One class per file
- **Methods**: Keep methods focused and small
- **Comments**: Use JavaDoc for public APIs

### Spring Boot Conventions
- **Controllers**: Use `@RestController` for REST APIs
- **Services**: Use `@Service` for business logic
- **Repositories**: Use `@Repository` for data access
- **Configuration**: Use `@Configuration` for configuration

## 🧪 Testing

### Test Types
- **Unit Tests**: Test individual components
- **Integration Tests**: Test component interactions
- **End-to-End Tests**: Test complete workflows
- **Performance Tests**: Test system performance

### Test Structure
```java
@SpringBootTest
class StudentServiceTest {
    
    @Autowired
    private StudentService studentService;
    
    @Test
    void shouldFindStudentByName() {
        // Given
        String name = "John Doe";
        
        // When
        List<Student> students = studentService.findByNameContaining(name);
        
        // Then
        assertThat(students).isNotEmpty();
        assertThat(students.get(0).getFirstName()).contains("John");
    }
}
```

### Test Guidelines
- **Test Coverage**: Aim for high test coverage
- **Test Names**: Use descriptive test names
- **Test Data**: Use realistic test data
- **Mocking**: Mock external dependencies
- **Assertions**: Use meaningful assertions

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=StudentServiceTest

# Run tests with coverage
mvn test jacoco:report
```

## 📚 Documentation

### Documentation Types
- **Code Documentation**: JavaDoc comments
- **API Documentation**: REST API documentation
- **User Documentation**: User guides and tutorials
- **Technical Documentation**: Architecture and design docs

### Documentation Standards
- **JavaDoc**: Use JavaDoc for all public APIs
- **Comments**: Add inline comments for complex logic
- **README**: Keep README up to date
- **Examples**: Include code examples

### Documentation Updates
- **New Features**: Document new features
- **API Changes**: Update API documentation
- **Configuration**: Document configuration changes
- **Examples**: Add usage examples

## 🐛 Issue Reporting

### Bug Reports
When reporting bugs, please include:
- **Description**: Clear description of the issue
- **Steps to Reproduce**: Detailed steps to reproduce
- **Expected Behavior**: What should happen
- **Actual Behavior**: What actually happens
- **Environment**: Java version, OS, etc.
- **Logs**: Relevant error logs

### Feature Requests
When requesting features, please include:
- **Description**: Clear description of the feature
- **Use Case**: Why this feature is needed
- **Proposed Solution**: How you think it should work
- **Alternatives**: Other solutions considered
- **Additional Context**: Any other relevant information

### Issue Template
```markdown
## Description
Brief description of the issue

## Steps to Reproduce
1. Go to '...'
2. Click on '....'
3. Scroll down to '....'
4. See error

## Expected Behavior
What should happen

## Actual Behavior
What actually happens

## Environment
- Java Version: 21.0.8
- OS: Ubuntu 22.04
- Browser: Chrome 120

## Additional Context
Any other relevant information
```

## 🔧 Development Workflow

### Daily Development
```bash
# Start development
git checkout main
git pull upstream main
git checkout -b feature/your-feature

# Make changes
# ... code changes ...

# Test changes
./build.sh

# Commit changes
git add .
git commit -m "feat: add student search functionality"

# Push changes
git push origin feature/your-feature
```

### Code Review
```bash
# Update branch
git checkout main
git pull upstream main
git checkout feature/your-feature
git rebase main

# Push updates
git push origin feature/your-feature
```

### Merging
```bash
# After PR is approved
git checkout main
git pull upstream main
git branch -d feature/your-feature
git push origin --delete feature/your-feature
```

## 🎯 Contribution Areas

### High Priority
- **Bug Fixes**: Critical bugs and issues
- **Security**: Security vulnerabilities
- **Performance**: Performance improvements
- **Documentation**: Missing or outdated documentation

### Medium Priority
- **New Features**: User-requested features
- **UI Improvements**: User interface enhancements
- **API Enhancements**: API improvements
- **Testing**: Test coverage improvements

### Low Priority
- **Code Refactoring**: Code quality improvements
- **Dependencies**: Dependency updates
- **Configuration**: Configuration improvements
- **Examples**: Code examples and tutorials

## 🏆 Recognition

### Contributors
We recognize and appreciate all contributors:
- **Code Contributors**: Developers who contribute code
- **Documentation Contributors**: People who improve documentation
- **Bug Reporters**: People who report bugs
- **Feature Requesters**: People who suggest features
- **Reviewers**: People who review code

### Recognition Methods
- **Contributors List**: Listed in README
- **Release Notes**: Mentioned in release notes
- **GitHub**: GitHub contributor statistics
- **Community**: Community recognition

## 📞 Getting Help

### Resources
- **Documentation**: Check the `docs/` folder
- **Issues**: Search existing issues
- **Discussions**: Use GitHub discussions
- **Community**: Join the community

### Contact
- **GitHub Issues**: Create issues for bugs and features
- **GitHub Discussions**: Use discussions for questions
- **Email**: Contact maintainers directly
- **Community**: Join community channels

## 📄 License

By contributing, you agree that your contributions will be licensed under the same license as the project.

## 🙏 Thank You

Thank you for contributing to the Music School Management System! Your contributions help make this project better for everyone.

---

**🎵 Happy contributing to the Music School Management System!**

*Together, we can build an amazing music school management system that helps music schools around the world manage their students, courses, and instructors more effectively.*
