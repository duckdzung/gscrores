import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Form, FormControl, FormField, FormItem, FormLabel, FormMessage } from '@/components/ui/form';
import { Input } from '@/components/ui/input';
import { zodResolver } from '@hookform/resolvers/zod';
import { useForm } from 'react-hook-form';
import { z } from 'zod';

export const searchFormSchema = z.object({
    registrationNumber: z.string().nonempty('Registration Number is required'),
});

export type SearchFormValues = z.infer<typeof searchFormSchema>;

interface SearchFormProps {
    onSubmit: (data: SearchFormValues) => Promise<void>;
    isLoading: boolean;
}

export function SearchForm({ onSubmit, isLoading }: SearchFormProps) {
    const form = useForm<SearchFormValues>({
        resolver: zodResolver(searchFormSchema),
        defaultValues: {
            registrationNumber: '',
        },
    });

    return (
        <Card className="w-full bg-sidebar">
            <CardHeader>
                <CardTitle>User Registration</CardTitle>
                <CardDescription>Enter student registration number to search for scores.</CardDescription>
            </CardHeader>
            <CardContent>
                <Form {...form}>
                    <form onSubmit={form.handleSubmit(onSubmit)}>
                        <div className="flex gap-2">
                            <FormField
                                control={form.control}
                                name="registrationNumber"
                                render={({ field }) => (
                                    <FormItem className="space-y-1 w-3/4">
                                        <FormLabel>Registration Number</FormLabel>
                                        <FormControl>
                                            <Input placeholder="Enter registration number" {...field} />
                                        </FormControl>
                                        <FormMessage />
                                    </FormItem>
                                )}
                                disabled={isLoading}
                            />
                            <div className="flex justify-center items-end">
                                <Button type="submit" disabled={isLoading}>
                                    Submit
                                </Button>
                            </div>
                        </div>
                    </form>
                </Form>
            </CardContent>
        </Card>
    );
}
