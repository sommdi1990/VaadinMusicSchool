package com.musicschool.ui;

import com.musicschool.entity.Course;
import com.musicschool.entity.Student;
import com.musicschool.service.CourseService;
import com.musicschool.service.StudentService;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.util.List;

/**
 * Dashboard view showing key metrics and statistics for the music school.
 */
@Route(value = "dashboard", layout = MainLayout.class)
@PageTitle("Dashboard | Music School Management")
public class DashboardView extends VerticalLayout {

    private final StudentService studentService;
    private final CourseService courseService;

    public DashboardView(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;

        setSizeFull();
        addClassName("dashboard-view");

        createHeader();
        createStatsCards();
        createCharts();
    }

    private void createHeader() {
        H2 header = new H2("Dashboard");
        header.addClassNames(LumoUtility.Margin.Bottom.LARGE, LumoUtility.Margin.Top.MEDIUM);
        add(header);
    }

    private void createStatsCards() {
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.addClassNames(LumoUtility.Margin.Bottom.LARGE);

        // Total Students
        Span totalStudents = createStatCard("Total Students", String.valueOf(studentService.count()), "👥");
        statsLayout.add(totalStudents);

        // Active Courses
        List<Course> activeCourses = courseService.findActiveCourses();
        Span activeCoursesCard = createStatCard("Active Courses", String.valueOf(activeCourses.size()), "📚");
        statsLayout.add(activeCoursesCard);

        // Students by Level
        long beginnerStudents = studentService.countByLevel(Student.StudentLevel.BEGINNER);
        Span beginnerCard = createStatCard("Beginner Students", String.valueOf(beginnerStudents), "🌱");
        statsLayout.add(beginnerCard);

        // Advanced Students
        long advancedStudents = studentService.countByLevel(Student.StudentLevel.ADVANCED);
        Span advancedCard = createStatCard("Advanced Students", String.valueOf(advancedStudents), "🎯");
        statsLayout.add(advancedCard);

        add(statsLayout);
    }

    private Span createStatCard(String title, String value, String icon) {
        VerticalLayout card = new VerticalLayout();
        card.addClassNames(
            LumoUtility.Background.BASE,
            LumoUtility.BorderRadius.MEDIUM,
            LumoUtility.Padding.LARGE,
            LumoUtility.BoxShadow.SMALL
        );
        card.setWidth("200px");

        Span iconSpan = new Span(icon);
        iconSpan.addClassNames(LumoUtility.FontSize.XXXLARGE);

        Span valueSpan = new Span(value);
        valueSpan.addClassNames(LumoUtility.FontSize.XXXLARGE, LumoUtility.FontWeight.BOLD);

        Span titleSpan = new Span(title);
        titleSpan.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);

        card.add(iconSpan, valueSpan, titleSpan);
        card.setAlignItems(FlexLayout.Alignment.CENTER);

        return new Span(card);
    }

    private void createCharts() {
        HorizontalLayout chartsLayout = new HorizontalLayout();
        chartsLayout.setWidthFull();

        // Students by Level Chart
        Chart studentsChart = createStudentsByLevelChart();
        studentsChart.setWidth("50%");
        chartsLayout.add(studentsChart);

        // Courses by Status Chart
        Chart coursesChart = createCoursesByStatusChart();
        coursesChart.setWidth("50%");
        chartsLayout.add(coursesChart);

        add(chartsLayout);
    }

    private Chart createStudentsByLevelChart() {
        Chart chart = new Chart(ChartType.PIE);
        Configuration config = chart.getConfiguration();
        config.setTitle("Students by Level");

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Beginner", studentService.countByLevel(Student.StudentLevel.BEGINNER)));
        series.add(new DataSeriesItem("Intermediate", studentService.countByLevel(Student.StudentLevel.INTERMEDIATE)));
        series.add(new DataSeriesItem("Advanced", studentService.countByLevel(Student.StudentLevel.ADVANCED)));
        series.add(new DataSeriesItem("Professional", studentService.countByLevel(Student.StudentLevel.PROFESSIONAL)));

        config.addSeries(series);
        return chart;
    }

    private Chart createCoursesByStatusChart() {
        Chart chart = new Chart(ChartType.COLUMN);
        Configuration config = chart.getConfiguration();
        config.setTitle("Courses by Status");

        DataSeries series = new DataSeries();
        series.add(new DataSeriesItem("Active", courseService.countByStatus(Course.CourseStatus.ACTIVE)));
        series.add(new DataSeriesItem("Inactive", courseService.countByStatus(Course.CourseStatus.INACTIVE)));
        series.add(new DataSeriesItem("Completed", courseService.countByStatus(Course.CourseStatus.COMPLETED)));
        series.add(new DataSeriesItem("Cancelled", courseService.countByStatus(Course.CourseStatus.CANCELLED)));

        config.addSeries(series);
        return chart;
    }
}
