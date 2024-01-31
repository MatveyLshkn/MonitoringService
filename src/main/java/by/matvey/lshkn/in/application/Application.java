package by.matvey.lshkn.in.application;

import by.matvey.lshkn.entity.Measurement;
import by.matvey.lshkn.entity.Meter;
import by.matvey.lshkn.entity.Role;
import by.matvey.lshkn.entity.User;
import by.matvey.lshkn.service.MeasurementService;
import by.matvey.lshkn.service.MeterService;
import by.matvey.lshkn.service.UserService;
import by.matvey.lshkn.util.LoggerUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main Console application
 */
public class Application {
    private final UserService userService = UserService.getInstance();
    private final MeterService meterService = MeterService.getInstance();
    private final MeasurementService measurementService = MeasurementService.getInstance();
    private Scanner scan = new Scanner(System.in);
    private String command;

    /**
     * Starts application
     */
    public void start() {
        mainMenu();
    }

    /**
     * <p>Main menu ui</p>
     * <p>Menu options:<p/>
     * <p>Register -> perform registration of user<p/>
     * <p>Authorize -> perform authorization of user<p/>
     * <p>Action menu -> leads to action menu depending on user role (Admin/User)<p/>
     * <p>Log out -> logs out current user<p/>
     * <p>Exit program ->  finishes work of application<p/>
     */
    private void mainMenu() {
        User currentUser = null;
        while (true) {
            System.out.println("""
                            Main Menu:
                    1. Register
                    2. Authorize
                    3. Action menu
                    4. Log out
                    5. Exit program
                    """);
            command = scan.nextLine();
            switch (command) {
                case "1": {
                    Optional<User> maybeUser = register();
                    if (maybeUser.isEmpty()) {
                        System.out.println("User already exists");
                        continue;
                    } else currentUser = maybeUser.get();
                    System.out.println("You are successfully registered as: " + currentUser.getUsername());
                    break;
                }
                case "2": {
                    Optional<User> maybeUser = authorize();
                    if (maybeUser.isEmpty()) {
                        System.out.println("Wrong password or user not found");
                        continue;
                    } else currentUser = maybeUser.get();
                    System.out.println("You are successfully authorized as: " + currentUser.getUsername());
                    break;
                }
                case "3": {
                    if (currentUser == null) {
                        System.out.println("You are not signed in!");
                        continue;
                    }
                    if (currentUser.getRole().equals(Role.ADMIN)) adminMenu();
                    else userMenu(currentUser);
                    break;
                }
                case "4": {
                    currentUser = null;
                    System.out.println("Successfully logged out!");
                    break;
                }
                case "5": {
                    scan.close();
                    return;
                }
                default: {
                    System.out.println("Wrong option, try again!");
                }
            }
        }
    }

    /**
     * Admin menu ui (Performs operations depending on admin input)
     * <p> Menu options:<p/>
     * <p>Add meter -> adds meter to list of available meters<p/>
     * <p>Print all measurements by username -> prints all measurements of specific user<p/>
     * <p>Print all users -> prints all users of system<p/>
     * <p>Get logs -> prints all application logs<p/>
     * <p>Return to main menu ->  returns to main menu<p/>
     */
    private void adminMenu() {
        while (true) {
            System.out.println("""
                            Admin Menu:
                    1. Add meter
                    2. Print all measurements by username
                    3. Print all users
                    4. Get logs
                    5. Return to main menu
                    """);
            command = scan.nextLine();
            switch (command) {
                case "1": {
                    System.out.println("Enter name of the new meter:");
                    String meterName = scan.nextLine();
                    if(meterService.getAvailableMeterNames().contains(meterName)){
                        System.out.println("Meter already exists!");
                        continue;
                    }
                    meterService.addNewMeterName(meterName);
                    System.out.println("Meter successfully added");
                    break;
                }
                case "2": {
                    System.out.println("Users list");
                    userService.getAllUsers().stream()
                            .filter(user -> !user.getRole().equals(Role.ADMIN))
                            .map(User::getUsername)
                            .forEach(System.out::println);

                    System.out.println("Enter one of usernames:");
                    String username = scan.nextLine();

                    userService.getUserByUsername(username)
                            .ifPresentOrElse(user -> measurementService.getAllMeasurements(user)
                                            .forEach(System.out::println),
                                    () -> System.out.println("No such user found!"));
                    break;
                }
                case "3": {
                    userService.getAllUsers().stream()
                            .map(User::getUsername)
                            .forEach(System.out::println);
                    break;
                }
                case "4": {
                    LoggerUtil.getLogInfo().forEach(System.out::println);
                    break;
                }
                case "5":
                    return;
                default: {
                    System.out.println("Wrong option, try again!");
                }
            }
        }
    }

    /**
     * User menu ui (Performs operations depending on user input)
     * <p> Menu options:<p/>
     * <p>Print relevant measurements -> prints relevant measurements of every meter for specific user<p/>
     * <p>Print measurements by month -> prints all measurements of specific user depending on month and year<p/>
     * <p>Print measurements history -> prints all measurements of specific user<p/>
     * <p>Submit measurement -> submits measurement from user<p/>
     * <p>Return to main menu ->  returns to main menu<p/>
     */
    private void userMenu(User currentUser) {
        while (true) {
            System.out.println("""
                            User Menu:
                    1. Print relevant measurements
                    2. Print measurements by month
                    3. Print measurements history
                    4. Submit measurement
                    5. Return to main menu
                    """);
            command = scan.nextLine();
            switch (command) {
                case "1": {
                    measurementService.getRelevantMeasurements(currentUser).forEach(System.out::println);
                    break;
                }
                case "2": {
                    int year;
                    while (true) {
                        System.out.println("Enter year:");
                        try {
                            year = Integer.parseInt(scan.nextLine());
                            break;
                        } catch (Exception e) {
                            System.out.println("Error, try again!");
                        }
                    }
                    System.out.println("Moths:");
                    Arrays.stream(Month.values()).forEach(System.out::println);
                    Month month;
                    while (true) {
                        System.out.println("Enter month:");
                        try {
                            month = Month.valueOf(scan.nextLine());
                            break;
                        } catch (Exception e) {
                            System.out.println("Error, try again!");
                        }
                    }
                    LocalDate date = LocalDate.of(year, month, 1);
                    measurementService.getMeasurementsByMonth(currentUser, date).forEach(System.out::println);
                    break;
                }
                case "3": {
                    measurementService.getAllMeasurements(currentUser).forEach(System.out::println);
                    break;
                }
                case "4": {
                    System.out.println("Available meters:");
                    meterService.getAvailableMeterNames().forEach(System.out::println);
                    Meter meter;
                    while (true) {
                        System.out.println("Enter one of meters name:");
                        String meterName = scan.nextLine();
                        Optional<Meter> maybeMeter = meterService.getMeter(meterName);
                        if (maybeMeter.isPresent()) {
                            meter = maybeMeter.get();
                            break;
                        } else {
                            System.out.println("Error, try again!");
                        }
                    }
                    double value;
                    while (true) {
                        System.out.println("Enter value");
                        try {
                            value = Double.parseDouble(scan.nextLine());
                            break;
                        } catch (Exception e) {
                            System.out.println("Error, try again!");
                        }
                    }
                    Measurement measurement = Measurement.builder()
                            .value(value)
                            .date(LocalDateTime.now())
                            .build();

                    currentUser.getMeters().stream()
                            .filter(userMeter -> userMeter.getName().equals(meter.getName()))
                            .findFirst()
                            .ifPresentOrElse(userMeter -> {
                                boolean result = measurementService.addMeasurement(userMeter, measurement);
                                if(result) System.out.println("Successfully added");
                                else System.out.println("Measurement is not valid: wrong date or value!");
                                    },
                                    () -> {
                                        meter.addMeasurement(measurement);
                                        currentUser.addMeter(meter);
                                    });
                    break;
                }
                case "5":
                    return;
                default: {
                    System.out.println("Wrong option, try again!");
                }
            }
        }
    }

    /**
     * Scans user input and registers user in system
     * @return Empty optional if user already exists otherwise optional of new user
     */
    protected Optional<User> register() {
        String username;
        String password;
        Role role;

        System.out.println("Enter username");
        username = scan.nextLine();
        System.out.println("Enter password");
        password = scan.nextLine();
        System.out.println("Enter one of the roles");
        Arrays.stream(Role.values()).forEach(System.out::println);
        while (true) {
            try {
                role = Role.valueOf(scan.nextLine());
                break;
            } catch (Exception e) {
                System.out.println("Enter valid role (example: USER)");
            }
        }
        User user = User.builder()
                .username(username)
                .password(password)
                .role(role)
                .build();

        if (userService.register(user)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    /**
     * Scans user input and authorizes user in the system
     * @return empty optional if user does not exist or wrong password otherwise return optional of authorized user
     */
    protected Optional<User> authorize() {
        String username;
        String password;

        System.out.println("Enter username");
        username = scan.nextLine();
        System.out.println("Enter password");
        password = scan.nextLine();
        User user = User.builder()
                .username(username)
                .password(password)
                .build();

        return userService.authenticate(user);
    }
}
