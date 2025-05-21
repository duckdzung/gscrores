import { PieChart, Pie, Cell, Legend, Tooltip, ResponsiveContainer } from 'recharts';

// Define a broader color palette for dynamic levels
const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#AF19FF', '#FF1919'];

// Styles cho Tooltip
const styles = {
    tooltipContainer: {
        backgroundColor: '#333',
        color: '#fff',
        padding: '10px',
        borderRadius: '8px',
        boxShadow: '0 4px 8px rgba(0, 0, 0, 0.2)',
    },
    tooltipTitle: {
        fontSize: '14px',
        fontWeight: 'bold',
        margin: 0,
    },
    tooltipValue: {
        fontSize: '12px',
        margin: 0,
    },
};

const CustomTooltip = ({ active, payload }: any) => {
    if (active && payload && payload.length) {
        const { name, value } = payload[0].payload;
        const fill = payload[0].payload.fill;
        return (
            <div style={{ ...styles.tooltipContainer, border: `2px solid ${fill}` }}>
                <p style={styles.tooltipTitle}>{`${name}`}</p>
                <p style={styles.tooltipValue}>{value} students</p>
            </div>
        );
    }
    return null;
};

const RADIAN = Math.PI / 180;
const renderCustomizedLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percent, name }: any) => {
    const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
    const x = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy + radius * Math.sin(-midAngle * RADIAN);

    if (percent < 0.05) return null; // Don't render label if too small

    return (
        <text x={x} y={y} fill="white" textAnchor={x > cx ? 'start' : 'end'} dominantBaseline="central">
            {`${name} (${(percent * 100).toFixed(0)}%)`}
        </text>
    );
};

export interface StatisticsChartData {
    name: string;
    count: number;
}

export function Statistics({ data }: { data: StatisticsChartData[] }) {
    const sortedData = [...data].sort((a, b) => {
        const numA = parseInt(a.name.replace('level', ''), 10);
        const numB = parseInt(b.name.replace('level', ''), 10);
        return numA - numB;
    });

    if (!data || data.length === 0) {
        return <p>No score distribution data available for this subject.</p>;
    }

    return (
        <div style={{ width: '100%', height: 400 }}>
            <ResponsiveContainer>
                <PieChart>
                    <Pie
                        data={sortedData}
                        cx="50%"
                        cy="50%"
                        labelLine={false}
                        label={renderCustomizedLabel}
                        outerRadius="80%"
                        fill="#8884d8"
                        dataKey="count"
                        nameKey="name"
                    >
                        {sortedData.map((_, index) => (
                            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                        ))}
                    </Pie>
                    <Tooltip content={<CustomTooltip />} />
                    <Legend
                        formatter={(value, entry: any) => {
                            const { name, count } = entry.payload?.payload || {};
                            if (name && count !== undefined) {
                                return `${name}: ${count}`;
                            }
                            return value;
                        }}
                    />
                </PieChart>
            </ResponsiveContainer>
        </div>
    );
}
