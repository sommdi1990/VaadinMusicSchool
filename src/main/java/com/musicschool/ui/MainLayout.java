package com.musicschool.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Main layout component for the Music School Management System.
 * Provides navigation and consistent layout across all views.
 */
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 logo = new H1("🎵 Music School Management");
        logo.addClassNames(
            LumoUtility.FontSize.LARGE,
            LumoUtility.Margin.MEDIUM
        );

        var header = new HorizontalLayout(new DrawerToggle(), logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
            LumoUtility.Padding.Vertical.NONE,
            LumoUtility.Padding.Horizontal.MEDIUM
        );

        addToNavbar(header);
    }

    private void createDrawer() {
        addToDrawer(new VerticalLayout(
            createNavigation()
        ));
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Dashboard", "dashboard", VaadinIcon.DASHBOARD.create()));
        nav.addItem(new SideNavItem("Students", "students", VaadinIcon.USER.create()));
        nav.addItem(new SideNavItem("Instructors", "instructors", VaadinIcon.ACADEMY_CAP.create()));
        nav.addItem(new SideNavItem("Courses", "courses", VaadinIcon.BOOK.create()));
        nav.addItem(new SideNavItem("Enrollments", "enrollments", VaadinIcon.CLIPBOARD.create()));
        nav.addItem(new SideNavItem("Reports", "reports", VaadinIcon.CHART.create()));
        nav.addItem(new SideNavItem("Settings", "settings", VaadinIcon.COG.create()));

        return nav;
    }
}
