package ExceptionsInProgrammingHW;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println(
                    "Enter your full name, date of birth, 11 digit phone number and sex separated by spaces in the format: " +
                            "LastName FirstName Patronymic dd.mm.yyyy 00000000000 m/f");
            String userData = scanner.nextLine();

            String[] data = userData.split(" ");
            if (data.length != 6) {
                throw new InvalidDataException("The amount of data entered does not match the required amount");
            }

            String lastName = data[0];
            String firstName = data[1];
            String patronymic = data[2];
            String birthDate = data[3];
            String phoneNumber = data[4];
            String sex = data[5];

            if (!isValidDate(birthDate)) {
                throw new InvalidDataException("Invalid date of birth format");
            }

            if (!isValidPhoneNumber(phoneNumber)) {
                throw new InvalidDataException("Invalid phone number format");
            }

            if (!isValidGender(sex)) {
                throw new InvalidDataException("Invalid sex value");
            }

            String filename = "UsersData.txt";
            String userDataLine = lastName + " " + firstName + " " + patronymic + " " + birthDate + " " + phoneNumber
                    + " " + sex;

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
                writer.write(userDataLine);
                writer.newLine();
            } catch (IOException e) {
                throw new FileAccessException("Error writing to file", e);
            }

            System.out.println("Data successfully written to file " + filename);
        } catch (InvalidDataException e) {
            System.out.println("Data validation error: " + e.getMessage());
        } catch (FileAccessException e) {
            System.out.println("File access error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        try {
            if (phoneNumber.length() == 11 && phoneNumber.matches("\\d+")) {
                Long.parseLong(phoneNumber);
                return true;
            }
            return false;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidGender(String gender) {
        return gender.equals("f") || gender.equals("m");
    }
}

class InvalidDataException extends RuntimeException {
    public InvalidDataException(String message) {
        super(message);
    }
}

class FileAccessException extends RuntimeException {
    public FileAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}