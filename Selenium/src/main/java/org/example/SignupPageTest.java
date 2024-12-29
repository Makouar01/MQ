package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SignupPageTest {
    public static void main(String[] args) {
        // Configure le chemin vers le ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

        // Initialise le WebDriver
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Ouvre la page d'inscription
            driver.get("http://localhost:4200/signup");

            // Maximiser la fenêtre
            driver.manage().window().maximize();

            // ** Scénario 1 : Champs vides **
            WebElement signupButton = driver.findElement(By.cssSelector("button[type='submit']"));
            signupButton.click();
            System.out.println("Btn");

            // Vérifie le message d'erreur pour le champ nom complet
            WebElement fullNameError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("p.text-red-500:nth-of-type(1)")));
            if (fullNameError.getText().equals("Le nom complet est requis")) {
                System.out.println("Test champs vides (nom complet) : Passé");
            }

            // Vérifie le message d'erreur pour le champ email
            WebElement emailError = driver.findElement(By.cssSelector("p.text-red-500:nth-of-type(2)"));
            if (emailError.getText().equals("L'email est requis")) {
                System.out.println("Test champs vides (email) : Passé");
            }

            // Vérifie le message d'erreur pour le champ mot de passe
            WebElement passwordError = driver.findElement(By.cssSelector("p.text-red-500:nth-of-type(3)"));
            if (passwordError.getText().equals("Le mot de passe est requis")) {
                System.out.println("Test champs vides (mot de passe) : Passé");
            }

            // Vérifie le message d'erreur pour le champ confirmation du mot de passe
            WebElement confirmPasswordError = driver.findElement(By.cssSelector("p.text-red-500:nth-of-type(4)"));
            if (confirmPasswordError.getText().equals("La confirmation du mot de passe est requise")) {
                System.out.println("Test champs vides (confirmation mot de passe) : Passé");
            }

            // ** Scénario 2 : Mots de passe ne correspondent pas **
            WebElement fullNameField = driver.findElement(By.id("fullName"));
            WebElement emailField = driver.findElement(By.id("email"));
            WebElement passwordField = driver.findElement(By.id("password"));
            WebElement confirmPasswordField = driver.findElement(By.id("confirmPassword"));

            fullNameField.sendKeys("John Doe");
            emailField.sendKeys("john.doe@example.com");
            passwordField.sendKeys("password123");
            confirmPasswordField.sendKeys("password321");
            signupButton.click();

            // Vérifie que l'erreur de non-correspondance des mots de passe est affichée
            WebElement passwordMismatchError = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".text-red-500")));
            if (passwordMismatchError.getText().equals("Les mots de passe ne correspondent pas.")) {
                System.out.println("Test mots de passe ne correspondent pas : Passé");
            }

            // ** Scénario 3 : Formulaire valide **
            confirmPasswordField.clear();
            confirmPasswordField.sendKeys("password123");
            signupButton.click();

            // Vérifie que l'inscription est réussie (redirection vers la page de connexion)
            wait.until(ExpectedConditions.urlToBe("http://localhost:4200/login"));
            System.out.println("Test inscription réussie : Passé");

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ferme le navigateur
            driver.quit();
        }
    }
}
