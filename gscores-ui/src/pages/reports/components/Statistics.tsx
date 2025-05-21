import { PieChart, Pie, Cell, Legend, Tooltip, ResponsiveContainer } from 'recharts';

// Define a broader color palette for dynamic levels
const COLORS = ['#FF8042', '#FFBB28', '#00C49F', '#0088FE', '#AF19FF'];

// Styles cho Tooltip
const styles = {
    tooltipContainer: {
        backgroundColor: 'rgba(51, 51, 51, 0.9)',
        color: '#fff',
        padding: '10px',
        borderRadius: '8px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
        border: 'none',
    },
    tooltipTitle: {
        fontSize: '14px',
        fontWeight: 'bold',
        margin: '0 0 5px 0',
    },
    tooltipValue: {
        fontSize: '12px',
        margin: 0,
    },
};

const CustomTooltip = ({ active, payload }: any) => {
    if (active && payload && payload.length) {
        const dataPoint = payload[0].payload;
        const name = dataPoint.name;
        const count = dataPoint.count;
        const color = payload[0].color || payload[0].payload.fill;

        // Safely access and calculate percentage
        const percentValue = payload[0].percent;
        let percentageString = '';
        if (typeof percentValue === 'number' && !isNaN(percentValue)) {
            percentageString = `(${(percentValue * 100).toFixed(0)}%)`;
        }
        // else, percentageString remains empty if percentValue is not a valid number

        return (
            <div style={{ ...styles.tooltipContainer, border: `1px solid ${color || '#ccc'}` }}>
                <p style={{ ...styles.tooltipTitle, color: color || '#fff' }}>{name}</p>
                <p style={styles.tooltipValue}>
                    {count} students {percentageString}
                </p>
            </div>
        );
    }
    return null;
};

export interface StatisticsChartData {
    name: string;
    count: number;
}

export function Statistics({ data }: { data: StatisticsChartData[] }) {
    if (!data || data.length === 0) {
        return <p>No score distribution data available for this subject.</p>;
    }

    return (
        <div style={{ width: '100%', height: 400 }}>
            <ResponsiveContainer>
                <PieChart>
                    <Pie
                        data={data}
                        cx="50%"
                        cy="50%"
                        labelLine={false}
                        label={false}
                        outerRadius="80%"
                        fill="#8884d8"
                        dataKey="count"
                        nameKey="name"
                    >
                        {data.map((_, index) => (
                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip content={<CustomTooltip />} />
                    <Legend
                        formatter={(value, entry: any) => {
                            const count = entry.payload?.payload?.count;
                            const percent = entry.payload?.percent;

                            let legendText = value;
                            if (count !== undefined) {
                                legendText += `: ${count}`;
                            }
                            if (percent !== undefined) {
                                legendText += ` (${(percent * 100).toFixed(0)}%)`;
                            }

                            return <span style={{ color: entry.color }}>{legendText}</span>;
                        }}
                    />
                </PieChart>
            </ResponsiveContainer>
        </div>
    );
}
