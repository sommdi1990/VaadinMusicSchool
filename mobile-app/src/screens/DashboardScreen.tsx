import React from 'react';
import {
  View,
  StyleSheet,
  ScrollView,
  RefreshControl,
} from 'react-native';
import {
  Card,
  Title,
  Paragraph,
  Button,
  Chip,
  Surface,
  Text,
} from 'react-native-paper';
import { LineChart, BarChart, PieChart } from 'react-native-chart-kit';
import { Dimensions } from 'react-native';
import { useAuthStore } from '../store/authStore';
import { useQuery } from 'react-query';
import { dashboardService } from '../services/dashboardService';

const screenWidth = Dimensions.get('window').width;

export default function DashboardScreen({ navigation }: any) {
  const { user } = useAuthStore();
  const [refreshing, setRefreshing] = React.useState(false);

  const { data: dashboardData, isLoading, refetch } = useQuery(
    'dashboard',
    dashboardService.getDashboardData,
    {
      refetchInterval: 30000, // Refresh every 30 seconds
    }
  );

  const onRefresh = React.useCallback(() => {
    setRefreshing(true);
    refetch().finally(() => setRefreshing(false));
  }, [refetch]);

  const chartConfig = {
    backgroundColor: '#2196F3',
    backgroundGradientFrom: '#2196F3',
    backgroundGradientTo: '#1976D2',
    decimalPlaces: 0,
    color: (opacity = 1) => `rgba(255, 255, 255, ${opacity})`,
    labelColor: (opacity = 1) => `rgba(255, 255, 255, ${opacity})`,
    style: {
      borderRadius: 16,
    },
    propsForDots: {
      r: '6',
      strokeWidth: '2',
      stroke: '#ffa726',
    },
  };

  const revenueData = {
    labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
    datasets: [
      {
        data: [20000, 25000, 30000, 28000, 35000, 40000],
        strokeWidth: 2,
      },
    ],
  };

  const enrollmentData = {
    labels: ['Beginner', 'Intermediate', 'Advanced', 'Professional'],
    datasets: [
      {
        data: [45, 30, 20, 15],
      },
    ],
  };

  return (
    <ScrollView
      style={styles.container}
      refreshControl={
        <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
      }
    >
      <View style={styles.header}>
        <Title>Welcome back, {user?.firstName}!</Title>
        <Paragraph>Here's your music school overview</Paragraph>
      </View>

      {/* Quick Stats */}
      <View style={styles.statsContainer}>
        <Surface style={styles.statCard}>
          <Text style={styles.statNumber}>156</Text>
          <Text style={styles.statLabel}>Total Students</Text>
        </Surface>
        <Surface style={styles.statCard}>
          <Text style={styles.statNumber}>24</Text>
          <Text style={styles.statLabel}>Active Courses</Text>
        </Surface>
        <Surface style={styles.statCard}>
          <Text style={styles.statNumber}>12</Text>
          <Text style={styles.statLabel}>Instructors</Text>
        </Surface>
      </View>

      {/* Revenue Chart */}
      <Card style={styles.chartCard}>
        <Card.Content>
          <Title>Monthly Revenue</Title>
          <LineChart
            data={revenueData}
            width={screenWidth - 60}
            height={220}
            chartConfig={chartConfig}
            bezier
            style={styles.chart}
          />
        </Card.Content>
      </Card>

      {/* Enrollment Distribution */}
      <Card style={styles.chartCard}>
        <Card.Content>
          <Title>Student Distribution</Title>
          <PieChart
            data={[
              { name: 'Beginner', population: 45, color: '#FF6B6B', legendFontColor: '#7F7F7F', legendFontSize: 12 },
              { name: 'Intermediate', population: 30, color: '#4ECDC4', legendFontColor: '#7F7F7F', legendFontSize: 12 },
              { name: 'Advanced', population: 20, color: '#45B7D1', legendFontColor: '#7F7F7F', legendFontSize: 12 },
              { name: 'Professional', population: 15, color: '#96CEB4', legendFontColor: '#7F7F7F', legendFontSize: 12 },
            ]}
            width={screenWidth - 60}
            height={220}
            chartConfig={chartConfig}
            accessor="population"
            backgroundColor="transparent"
            paddingLeft="15"
            center={[10, 0]}
          />
        </Card.Content>
      </Card>

      {/* Quick Actions */}
      <Card style={styles.actionCard}>
        <Card.Content>
          <Title>Quick Actions</Title>
          <View style={styles.actionButtons}>
            <Button
              mode="contained"
              onPress={() => navigation.navigate('Schedule')}
              style={styles.actionButton}
            >
              View Schedule
            </Button>
            <Button
              mode="outlined"
              onPress={() => navigation.navigate('Payment')}
              style={styles.actionButton}
            >
              Make Payment
            </Button>
            <Button
              mode="outlined"
              onPress={() => navigation.navigate('Report')}
              style={styles.actionButton}
            >
              View Reports
            </Button>
          </View>
        </Card.Content>
      </Card>

      {/* Recent Activity */}
      <Card style={styles.activityCard}>
        <Card.Content>
          <Title>Recent Activity</Title>
          <View style={styles.activityItem}>
            <Chip icon="check" style={styles.activityChip}>
              Payment received from John Doe
            </Chip>
          </View>
          <View style={styles.activityItem}>
            <Chip icon="schedule" style={styles.activityChip}>
              Piano lesson scheduled for 3:00 PM
            </Chip>
          </View>
          <View style={styles.activityItem}>
            <Chip icon="person-add" style={styles.activityChip}>
              New student enrolled in Guitar course
            </Chip>
          </View>
        </Card.Content>
      </Card>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#f5f5f5',
  },
  header: {
    padding: 20,
    paddingBottom: 10,
  },
  statsContainer: {
    flexDirection: 'row',
    justifyContent: 'space-around',
    paddingHorizontal: 20,
    marginBottom: 20,
  },
  statCard: {
    flex: 1,
    marginHorizontal: 5,
    padding: 15,
    alignItems: 'center',
    elevation: 2,
    borderRadius: 8,
  },
  statNumber: {
    fontSize: 24,
    fontWeight: 'bold',
    color: '#2196F3',
  },
  statLabel: {
    fontSize: 12,
    color: '#666',
    marginTop: 4,
  },
  chartCard: {
    margin: 10,
    elevation: 4,
  },
  chart: {
    marginVertical: 8,
    borderRadius: 16,
  },
  actionCard: {
    margin: 10,
    elevation: 4,
  },
  actionButtons: {
    flexDirection: 'row',
    flexWrap: 'wrap',
    justifyContent: 'space-around',
    marginTop: 10,
  },
  actionButton: {
    margin: 5,
    minWidth: 100,
  },
  activityCard: {
    margin: 10,
    elevation: 4,
  },
  activityItem: {
    marginVertical: 5,
  },
  activityChip: {
    alignSelf: 'flex-start',
  },
});
