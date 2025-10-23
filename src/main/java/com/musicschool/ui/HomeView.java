package com.musicschool.ui;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;


/**
 * Home page view for the Music School Management System.
 * Landing page inspired by modern music school websites.
 */
@Route(value = "", layout = PublicLayout.class)
@PageTitle("Home | Music School Management")
public class HomeView extends VerticalLayout {

    public HomeView() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("home-view");

        createHeroSection();
        createInstrumentsSection();
        createAboutSection();
        createTeamSection();
        createTestimonialsSection();
        createArticlesSection();
        createContactSection();
        createFooter();
    }

    private void createHeroSection() {
        VerticalLayout heroLayout = new VerticalLayout();
        heroLayout.setWidthFull();
        heroLayout.setPadding(true);
        heroLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        heroLayout.addClassName("hero-section");
        heroLayout.getStyle().set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)");
        heroLayout.getStyle().set("color", "white");
        heroLayout.getStyle().set("min-height", "70vh");
        heroLayout.getStyle().set("padding", "4rem 2rem");

        H1 mainTitle = new H1("For Those About to Rock!");
        mainTitle.addClassName(LumoUtility.FontSize.XXXLARGE);
        mainTitle.getStyle().set("font-weight", "bold");
        mainTitle.getStyle().set("text-align", "center");
        mainTitle.getStyle().set("margin-bottom", "1rem");

        H2 subtitle = new H2("Free Trial Lesson");
        subtitle.addClassName(LumoUtility.FontSize.XLARGE);
        subtitle.getStyle().set("text-align", "center");
        subtitle.getStyle().set("margin-bottom", "2rem");
        subtitle.getStyle().set("opacity", "0.9");

        Paragraph description = new Paragraph(
                "Learn to play music quickly and effectively with personalized music lessons. " +
                        "At our music school, you get lessons from highly skilled and experienced instructors. " +
                        "At your level, at your own pace."
        );
        description.addClassName(LumoUtility.FontSize.LARGE);
        description.getStyle().set("text-align", "center");
        description.getStyle().set("max-width", "600px");
        description.getStyle().set("margin-bottom", "3rem");
        description.getStyle().set("opacity", "0.9");

        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonLayout.setSpacing(true);

        Button trialButton = new Button("Request Trial Lesson", VaadinIcon.PLAY.create());
        trialButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        trialButton.getStyle().set("background", "white");
        trialButton.getStyle().set("color", "#667eea");
        trialButton.getStyle().set("font-weight", "bold");
        trialButton.addClickListener(e -> {
            // TODO: Navigate to contact form or trial lesson request page
        });

        Button learnMoreButton = new Button("Learn More", VaadinIcon.INFO.create());
        learnMoreButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST, ButtonVariant.LUMO_LARGE);
        learnMoreButton.getStyle().set("border", "2px solid white");
        learnMoreButton.getStyle().set("color", "white");
        learnMoreButton.addClickListener(e -> {
            // TODO: Navigate to about section or scroll down
        });

        buttonLayout.add(trialButton, learnMoreButton);
        heroLayout.add(mainTitle, subtitle, description, buttonLayout);
        add(heroLayout);
    }

    private void createInstrumentsSection() {
        VerticalLayout instrumentsLayout = new VerticalLayout();
        instrumentsLayout.setWidthFull();
        instrumentsLayout.setPadding(true);
        instrumentsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        instrumentsLayout.addClassName("instruments-section");
        instrumentsLayout.getStyle().set("background", "#f8f9fa");
        instrumentsLayout.getStyle().set("padding", "4rem 2rem");

        H2 sectionTitle = new H2("Music Lessons for Everyone");
        sectionTitle.addClassName(LumoUtility.FontSize.XXXLARGE);
        sectionTitle.getStyle().set("text-align", "center");
        sectionTitle.getStyle().set("margin-bottom", "1rem");
        sectionTitle.getStyle().set("color", "#333");

        H3 sectionSubtitle = new H3("MUSIC SCHOOL");
        sectionSubtitle.addClassName(LumoUtility.FontSize.LARGE);
        sectionSubtitle.getStyle().set("text-align", "center");
        sectionSubtitle.getStyle().set("margin-bottom", "2rem");
        sectionSubtitle.getStyle().set("color", "#666");
        sectionSubtitle.getStyle().set("font-weight", "normal");

        Paragraph sectionDescription = new Paragraph(
                "At our music school, you get music lessons for the instruments below, in all music styles."
        );
        sectionDescription.addClassName(LumoUtility.FontSize.LARGE);
        sectionDescription.getStyle().set("text-align", "center");
        sectionDescription.getStyle().set("max-width", "600px");
        sectionDescription.getStyle().set("margin-bottom", "3rem");
        sectionDescription.getStyle().set("color", "#666");

        // Instruments Grid
        FlexLayout instrumentsGrid = new FlexLayout();
        instrumentsGrid.setWidthFull();
        instrumentsGrid.getStyle().set("max-width", "1200px");
        instrumentsGrid.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        instrumentsGrid.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        instrumentsGrid.getStyle().set("gap", "1rem");

        String[] instruments = {
                "Piano", "Guitar", "Violin", "Drums", "Bass", "Saxophone",
                "Flute", "Trumpet", "Cello", "Keyboard", "Vocals", "Ukulele"
        };

        VaadinIcon[] instrumentIcons = {
                VaadinIcon.MUSIC, VaadinIcon.MUSIC, VaadinIcon.MUSIC, VaadinIcon.MUSIC,
                VaadinIcon.MUSIC, VaadinIcon.MUSIC, VaadinIcon.MUSIC, VaadinIcon.MUSIC,
                VaadinIcon.MUSIC, VaadinIcon.MUSIC, VaadinIcon.MUSIC, VaadinIcon.MUSIC
        };

        for (int i = 0; i < instruments.length; i++) {
            VerticalLayout instrumentCard = createInstrumentCard(instruments[i], instrumentIcons[i]);
            instrumentsGrid.add(instrumentCard);
        }

        Button viewAllButton = new Button("View All Music Lessons", VaadinIcon.ARROW_RIGHT.create());
        viewAllButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        viewAllButton.getStyle().set("margin-top", "2rem");

        instrumentsLayout.add(sectionTitle, sectionSubtitle, sectionDescription, instrumentsGrid, viewAllButton);
        add(instrumentsLayout);
    }

    private VerticalLayout createInstrumentCard(String instrumentName, VaadinIcon icon) {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("200px");
        card.setHeight("250px");
        card.setPadding(true);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        card.getStyle().set("background", "white");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");
        card.getStyle().set("margin", "0.5rem");
        card.getStyle().set("cursor", "pointer");
        card.getStyle().set("transition", "transform 0.2s");

        card.addClickListener(e -> {
            card.getStyle().set("transform", "translateY(-2px)");
        });

        Icon instrumentIcon = icon.create();
        instrumentIcon.setSize("48px");
        instrumentIcon.getStyle().set("color", "#667eea");
        instrumentIcon.getStyle().set("margin-bottom", "1rem");

        H3 instrumentTitle = new H3(instrumentName);
        instrumentTitle.addClassName(LumoUtility.FontSize.LARGE);
        instrumentTitle.getStyle().set("text-align", "center");
        instrumentTitle.getStyle().set("margin", "0");
        instrumentTitle.getStyle().set("color", "#333");

        Paragraph price = new Paragraph("From $136/quarter");
        price.addClassName(LumoUtility.FontSize.SMALL);
        price.getStyle().set("text-align", "center");
        price.getStyle().set("margin", "0.5rem 0 0 0");
        price.getStyle().set("color", "#666");

        card.add(instrumentIcon, instrumentTitle, price);
        return card;
    }

    private void createAboutSection() {
        VerticalLayout aboutLayout = new VerticalLayout();
        aboutLayout.setWidthFull();
        aboutLayout.setPadding(true);
        aboutLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        aboutLayout.addClassName("about-section");
        aboutLayout.getStyle().set("padding", "4rem 2rem");

        H2 sectionTitle = new H2("About Our Music School");
        sectionTitle.addClassName(LumoUtility.FontSize.XXXLARGE);
        sectionTitle.getStyle().set("text-align", "center");
        sectionTitle.getStyle().set("margin-bottom", "2rem");
        sectionTitle.getStyle().set("color", "#333");

        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setWidthFull();
        contentLayout.setMaxWidth("1000px");
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        contentLayout.setSpacing(true);

        // Stats
        VerticalLayout statsLayout = new VerticalLayout();
        statsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        statsLayout.setSpacing(true);

        String[] stats = {"6", "10", "50", "900", "10"};
        String[] statLabels = {"Locations", "Instruments", "Instructors", "Students", "Rating"};

        for (int i = 0; i < stats.length; i++) {
            VerticalLayout statItem = new VerticalLayout();
            statItem.setAlignItems(FlexComponent.Alignment.CENTER);
            statItem.setSpacing(false);

            H1 statNumber = new H1(stats[i]);
            statNumber.addClassName(LumoUtility.FontSize.XXXLARGE);
            statNumber.getStyle().set("color", "#667eea");
            statNumber.getStyle().set("margin", "0");

            Paragraph statLabel = new Paragraph(statLabels[i]);
            statLabel.addClassName(LumoUtility.FontSize.MEDIUM);
            statLabel.getStyle().set("color", "#666");
            statLabel.getStyle().set("margin", "0");

            statItem.add(statNumber, statLabel);
            statsLayout.add(statItem);
        }

        // Description
        VerticalLayout descriptionLayout = new VerticalLayout();
        descriptionLayout.setSpacing(true);

        Paragraph description = new Paragraph(
                "Our music school is more often than you think the music school close to home. " +
                        "We believe in providing quality music education to students of all ages and skill levels. " +
                        "Our experienced instructors are passionate about music and dedicated to helping you achieve your musical goals."
        );
        description.addClassName(LumoUtility.FontSize.LARGE);
        description.getStyle().set("color", "#666");
        description.getStyle().set("line-height", "1.6");

        Paragraph mission = new Paragraph(
                "Whether you're a beginner taking your first steps in music or an advanced student looking to refine your skills, " +
                        "we have the right program for you. Our comprehensive curriculum covers classical, jazz, rock, pop, and many other genres."
        );
        mission.addClassName(LumoUtility.FontSize.LARGE);
        mission.getStyle().set("color", "#666");
        mission.getStyle().set("line-height", "1.6");

        descriptionLayout.add(description, mission);
        contentLayout.add(statsLayout, descriptionLayout);
        aboutLayout.add(sectionTitle, contentLayout);
        add(aboutLayout);
    }

    private void createTeamSection() {
        VerticalLayout teamLayout = new VerticalLayout();
        teamLayout.setWidthFull();
        teamLayout.setPadding(true);
        teamLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        teamLayout.addClassName("team-section");
        teamLayout.getStyle().set("background", "#f8f9fa");
        teamLayout.getStyle().set("padding", "4rem 2rem");

        H2 sectionTitle = new H2("Our Team");
        sectionTitle.addClassName(LumoUtility.FontSize.XXXLARGE);
        sectionTitle.getStyle().set("text-align", "center");
        sectionTitle.getStyle().set("margin-bottom", "1rem");
        sectionTitle.getStyle().set("color", "#333");

        Paragraph sectionDescription = new Paragraph(
                "As a student at our music school, you are always in good hands. " +
                        "Our team consists of driven professional musicians."
        );
        sectionDescription.addClassName(LumoUtility.FontSize.LARGE);
        sectionDescription.getStyle().set("text-align", "center");
        sectionDescription.getStyle().set("max-width", "600px");
        sectionDescription.getStyle().set("margin-bottom", "3rem");
        sectionDescription.getStyle().set("color", "#666");

        FlexLayout teamGrid = new FlexLayout();
        teamGrid.setWidthFull();
        teamGrid.getStyle().set("max-width", "1000px");
        teamGrid.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        teamGrid.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        teamGrid.getStyle().set("gap", "1rem");

        String[] instructors = {"John Smith", "Sarah Johnson", "Mike Davis", "Lisa Wilson", "David Brown", "Emma Taylor"};
        String[] specializations = {"Piano & Keyboard", "Guitar & Bass", "Drums & Percussion", "Violin & Strings", "Vocals & Singing", "Saxophone & Woodwinds"};
        String[] locations = {"Downtown", "North Campus", "South Campus", "East Campus", "West Campus", "Central Campus"};

        for (int i = 0; i < instructors.length; i++) {
            VerticalLayout instructorCard = createInstructorCard(instructors[i], specializations[i], locations[i]);
            teamGrid.add(instructorCard);
        }

        teamLayout.add(sectionTitle, sectionDescription, teamGrid);
        add(teamLayout);
    }

    private VerticalLayout createInstructorCard(String name, String specialization, String location) {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("250px");
        card.setPadding(true);
        card.setAlignItems(FlexComponent.Alignment.CENTER);
        card.getStyle().set("background", "white");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");
        card.getStyle().set("margin", "0.5rem");

        // Placeholder for instructor photo
        Div photoPlaceholder = new Div();
        photoPlaceholder.setWidth("120px");
        photoPlaceholder.setHeight("120px");
        photoPlaceholder.getStyle().set("background", "#667eea");
        photoPlaceholder.getStyle().set("border-radius", "50%");
        photoPlaceholder.getStyle().set("margin-bottom", "1rem");
        photoPlaceholder.getStyle().set("display", "flex");
        photoPlaceholder.getStyle().set("align-items", "center");
        photoPlaceholder.getStyle().set("justify-content", "center");
        photoPlaceholder.getStyle().set("color", "white");
        photoPlaceholder.getStyle().set("font-size", "2rem");
        photoPlaceholder.setText(name.split(" ")[0].substring(0, 1) + name.split(" ")[1].substring(0, 1));

        H3 instructorName = new H3(name);
        instructorName.addClassName(LumoUtility.FontSize.LARGE);
        instructorName.getStyle().set("text-align", "center");
        instructorName.getStyle().set("margin", "0 0 0.5rem 0");
        instructorName.getStyle().set("color", "#333");

        Paragraph specializationText = new Paragraph(specialization);
        specializationText.addClassName(LumoUtility.FontSize.MEDIUM);
        specializationText.getStyle().set("text-align", "center");
        specializationText.getStyle().set("margin", "0 0 0.5rem 0");
        specializationText.getStyle().set("color", "#667eea");
        specializationText.getStyle().set("font-weight", "bold");

        Paragraph locationText = new Paragraph(location);
        locationText.addClassName(LumoUtility.FontSize.SMALL);
        locationText.getStyle().set("text-align", "center");
        locationText.getStyle().set("margin", "0");
        locationText.getStyle().set("color", "#666");

        card.add(photoPlaceholder, instructorName, specializationText, locationText);
        return card;
    }

    private void createTestimonialsSection() {
        VerticalLayout testimonialsLayout = new VerticalLayout();
        testimonialsLayout.setWidthFull();
        testimonialsLayout.setPadding(true);
        testimonialsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        testimonialsLayout.addClassName("testimonials-section");
        testimonialsLayout.getStyle().set("padding", "4rem 2rem");

        H2 sectionTitle = new H2("Student Reviews");
        sectionTitle.addClassName(LumoUtility.FontSize.XXXLARGE);
        sectionTitle.getStyle().set("text-align", "center");
        sectionTitle.getStyle().set("margin-bottom", "3rem");
        sectionTitle.getStyle().set("color", "#333");

        FlexLayout testimonialsGrid = new FlexLayout();
        testimonialsGrid.setWidthFull();
        testimonialsGrid.getStyle().set("max-width", "1000px");
        testimonialsGrid.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        testimonialsGrid.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        testimonialsGrid.getStyle().set("gap", "1rem");

        String[] testimonials = {
                "Clear communication, good atmosphere and an instructor with passion for music. Most important is the great connection with our child.",
                "Excellent music school, extremely experienced instructors and always super fun!",
                "I've been taking vocal lessons for over 4 years now and I'm in a progressive mood. It's a great addition to my main instrument, the piano.",
                "The instructors are patient and knowledgeable. My daughter has improved tremendously in just a few months.",
                "Great variety of instruments and flexible scheduling. Perfect for working professionals.",
                "The music school has helped me discover my passion for jazz. Highly recommended!"
        };

        String[] authors = {"Sarah M.", "John D.", "Maria L.", "Robert K.", "Jennifer W.", "Alex P."};
        String[] roles = {"Parent", "Student", "Student", "Parent", "Student", "Student"};

        for (int i = 0; i < testimonials.length; i++) {
            VerticalLayout testimonialCard = createTestimonialCard(testimonials[i], authors[i], roles[i]);
            testimonialsGrid.add(testimonialCard);
        }

        testimonialsLayout.add(sectionTitle, testimonialsGrid);
        add(testimonialsLayout);
    }

    private VerticalLayout createTestimonialCard(String testimonial, String author, String role) {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("300px");
        card.setPadding(true);
        card.getStyle().set("background", "white");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");
        card.getStyle().set("margin", "0.5rem");

        Icon quoteIcon = VaadinIcon.QUOTE_LEFT.create();
        quoteIcon.setSize("24px");
        quoteIcon.getStyle().set("color", "#667eea");
        quoteIcon.getStyle().set("margin-bottom", "1rem");

        Paragraph testimonialText = new Paragraph(testimonial);
        testimonialText.addClassName(LumoUtility.FontSize.MEDIUM);
        testimonialText.getStyle().set("color", "#666");
        testimonialText.getStyle().set("line-height", "1.6");
        testimonialText.getStyle().set("margin", "0 0 1rem 0");

        HorizontalLayout authorLayout = new HorizontalLayout();
        authorLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        authorLayout.setSpacing(true);

        Div authorPhoto = new Div();
        authorPhoto.setWidth("40px");
        authorPhoto.setHeight("40px");
        authorPhoto.getStyle().set("background", "#667eea");
        authorPhoto.getStyle().set("border-radius", "50%");
        authorPhoto.getStyle().set("display", "flex");
        authorPhoto.getStyle().set("align-items", "center");
        authorPhoto.getStyle().set("justify-content", "center");
        authorPhoto.getStyle().set("color", "white");
        authorPhoto.getStyle().set("font-weight", "bold");
        authorPhoto.setText(author.substring(0, 1));

        VerticalLayout authorInfo = new VerticalLayout();
        authorInfo.setSpacing(false);
        authorInfo.setPadding(false);

        Paragraph authorName = new Paragraph(author);
        authorName.addClassName(LumoUtility.FontSize.MEDIUM);
        authorName.getStyle().set("font-weight", "bold");
        authorName.getStyle().set("margin", "0");
        authorName.getStyle().set("color", "#333");

        Paragraph authorRole = new Paragraph(role);
        authorRole.addClassName(LumoUtility.FontSize.SMALL);
        authorRole.getStyle().set("margin", "0");
        authorRole.getStyle().set("color", "#666");

        authorInfo.add(authorName, authorRole);
        authorLayout.add(authorPhoto, authorInfo);

        card.add(quoteIcon, testimonialText, authorLayout);
        return card;
    }

    private void createArticlesSection() {
        VerticalLayout articlesLayout = new VerticalLayout();
        articlesLayout.setWidthFull();
        articlesLayout.setPadding(true);
        articlesLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        articlesLayout.addClassName("articles-section");
        articlesLayout.getStyle().set("background", "#f8f9fa");
        articlesLayout.getStyle().set("padding", "4rem 2rem");

        H2 sectionTitle = new H2("Latest News & Articles");
        sectionTitle.addClassName(LumoUtility.FontSize.XXXLARGE);
        sectionTitle.getStyle().set("text-align", "center");
        sectionTitle.getStyle().set("margin-bottom", "3rem");
        sectionTitle.getStyle().set("color", "#333");

        FlexLayout articlesGrid = new FlexLayout();
        articlesGrid.setWidthFull();
        articlesGrid.getStyle().set("max-width", "1000px");
        articlesGrid.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        articlesGrid.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        articlesGrid.getStyle().set("gap", "1rem");

        String[] articleTitles = {
                "Music Education Benefits for Children",
                "Choosing the Right Instrument for Beginners",
                "Practice Tips for Music Students",
                "The Importance of Music Theory",
                "Performance Anxiety: How to Overcome It",
                "Music and Brain Development"
        };

        String[] articleDates = {
                "March 15, 2024", "March 10, 2024", "March 5, 2024",
                "February 28, 2024", "February 20, 2024", "February 15, 2024"
        };

        String[] articleExcerpts = {
                "Discover how music education can enhance cognitive development and creativity in children...",
                "A comprehensive guide to help parents and students choose the perfect first instrument...",
                "Effective practice techniques that will help you improve faster and more efficiently...",
                "Understanding music theory is crucial for becoming a well-rounded musician...",
                "Learn practical strategies to manage performance anxiety and boost confidence...",
                "Scientific research shows the positive impact of music on brain development..."
        };

        for (int i = 0; i < articleTitles.length; i++) {
            VerticalLayout articleCard = createArticleCard(articleTitles[i], articleDates[i], articleExcerpts[i]);
            articlesGrid.add(articleCard);
        }

        Button viewAllArticlesButton = new Button("View All Articles", VaadinIcon.ARROW_RIGHT.create());
        viewAllArticlesButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        viewAllArticlesButton.getStyle().set("margin-top", "2rem");

        articlesLayout.add(sectionTitle, articlesGrid, viewAllArticlesButton);
        add(articlesLayout);
    }

    private VerticalLayout createArticleCard(String title, String date, String excerpt) {
        VerticalLayout card = new VerticalLayout();
        card.setWidth("300px");
        card.setPadding(true);
        card.getStyle().set("background", "white");
        card.getStyle().set("border-radius", "8px");
        card.getStyle().set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");
        card.getStyle().set("margin", "0.5rem");
        card.getStyle().set("cursor", "pointer");

        card.addClickListener(e -> {
            // TODO: Navigate to article detail page
        });

        Paragraph dateText = new Paragraph(date);
        dateText.addClassName(LumoUtility.FontSize.SMALL);
        dateText.getStyle().set("color", "#667eea");
        dateText.getStyle().set("margin", "0 0 0.5rem 0");
        dateText.getStyle().set("font-weight", "bold");

        H3 articleTitle = new H3(title);
        articleTitle.addClassName(LumoUtility.FontSize.LARGE);
        articleTitle.getStyle().set("margin", "0 0 1rem 0");
        articleTitle.getStyle().set("color", "#333");
        articleTitle.getStyle().set("line-height", "1.3");

        Paragraph excerptText = new Paragraph(excerpt);
        excerptText.addClassName(LumoUtility.FontSize.MEDIUM);
        excerptText.getStyle().set("color", "#666");
        excerptText.getStyle().set("line-height", "1.5");
        excerptText.getStyle().set("margin", "0 0 1rem 0");

        Button readMoreButton = new Button("Read More", VaadinIcon.ARROW_RIGHT.create());
        readMoreButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        readMoreButton.getStyle().set("color", "#667eea");
        readMoreButton.getStyle().set("padding", "0");

        card.add(dateText, articleTitle, excerptText, readMoreButton);
        return card;
    }

    private void createContactSection() {
        VerticalLayout contactLayout = new VerticalLayout();
        contactLayout.setWidthFull();
        contactLayout.setPadding(true);
        contactLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        contactLayout.addClassName("contact-section");
        contactLayout.getStyle().set("padding", "4rem 2rem");

        H2 sectionTitle = new H2("Contact Us");
        sectionTitle.addClassName(LumoUtility.FontSize.XXXLARGE);
        sectionTitle.getStyle().set("text-align", "center");
        sectionTitle.getStyle().set("margin-bottom", "2rem");
        sectionTitle.getStyle().set("color", "#333");

        HorizontalLayout contactContent = new HorizontalLayout();
        contactContent.setWidthFull();
        contactContent.setMaxWidth("1000px");
        contactContent.setAlignItems(FlexComponent.Alignment.START);
        contactContent.setSpacing(true);

        // Contact Information
        VerticalLayout contactInfo = new VerticalLayout();
        contactInfo.setSpacing(true);

        H3 contactInfoTitle = new H3("Get in Touch");
        contactInfoTitle.addClassName(LumoUtility.FontSize.XLARGE);
        contactInfoTitle.getStyle().set("color", "#333");
        contactInfoTitle.getStyle().set("margin-bottom", "1rem");

        String[] contactDetails = {
                "📧 info@musicschool.com",
                "📞 (555) 123-4567",
                "📍 123 Music Street, City, State 12345",
                "🕒 Mon-Fri: 9AM-6PM, Sat: 10AM-4PM"
        };

        for (String detail : contactDetails) {
            Paragraph contactDetail = new Paragraph(detail);
            contactDetail.addClassName(LumoUtility.FontSize.LARGE);
            contactDetail.getStyle().set("color", "#666");
            contactDetail.getStyle().set("margin", "0.5rem 0");
            contactInfo.add(contactDetail);
        }

        // Contact Form
        VerticalLayout contactForm = new VerticalLayout();
        contactForm.setSpacing(true);

        H3 formTitle = new H3("Send us a Message");
        formTitle.addClassName(LumoUtility.FontSize.XLARGE);
        formTitle.getStyle().set("color", "#333");
        formTitle.getStyle().set("margin-bottom", "1rem");

        TextField nameField = new TextField("Full Name");
        nameField.setWidthFull();

        EmailField emailField = new EmailField("Email");
        emailField.setWidthFull();

        TextField subjectField = new TextField("Subject");
        subjectField.setWidthFull();

        TextField messageField = new TextField("Message");
        messageField.setWidthFull();
        messageField.getStyle().set("height", "100px");

        Button sendButton = new Button("Send Message", VaadinIcon.PAPERPLANE.create());
        sendButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        sendButton.setWidthFull();

        contactForm.add(formTitle, nameField, emailField, subjectField, messageField, sendButton);
        contactContent.add(contactInfo, contactForm);
        contactLayout.add(sectionTitle, contactContent);
        add(contactLayout);
    }

    private void createFooter() {
        VerticalLayout footerLayout = new VerticalLayout();
        footerLayout.setWidthFull();
        footerLayout.setPadding(true);
        footerLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        footerLayout.addClassName("footer-section");
        footerLayout.getStyle().set("background", "#333");
        footerLayout.getStyle().set("color", "white");
        footerLayout.getStyle().set("padding", "2rem");

        H3 footerTitle = new H3("Music School Management");
        footerTitle.addClassName(LumoUtility.FontSize.XLARGE);
        footerTitle.getStyle().set("margin", "0 0 1rem 0");

        Paragraph footerDescription = new Paragraph(
                "Providing quality music education for students of all ages and skill levels."
        );
        footerDescription.addClassName(LumoUtility.FontSize.MEDIUM);
        footerDescription.getStyle().set("text-align", "center");
        footerDescription.getStyle().set("margin", "0 0 1rem 0");
        footerDescription.getStyle().set("opacity", "0.8");

        HorizontalLayout footerLinks = new HorizontalLayout();
        footerLinks.setSpacing(true);
        footerLinks.setAlignItems(FlexComponent.Alignment.CENTER);

        String[] footerLinkTexts = {"Privacy Policy", "Terms of Service", "Contact", "About Us"};
        for (String linkText : footerLinkTexts) {
            Button footerLink = new Button(linkText);
            footerLink.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            footerLink.getStyle().set("color", "white");
            footerLink.getStyle().set("text-decoration", "underline");
            footerLinks.add(footerLink);
        }

        Paragraph copyright = new Paragraph("© 2024 Music School Management. All rights reserved.");
        copyright.addClassName(LumoUtility.FontSize.SMALL);
        copyright.getStyle().set("text-align", "center");
        copyright.getStyle().set("margin", "1rem 0 0 0");
        copyright.getStyle().set("opacity", "0.6");

        footerLayout.add(footerTitle, footerDescription, footerLinks, copyright);
        add(footerLayout);
    }

}
