package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPageTest {

    public static void main(String[] args) {
        // Configuration du WebDriver (chemin vers le driver Chrome)
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe"); // Remplacez par le chemin de votre driver
        WebDriver driver = new ChromeDriver();

        try {
            // Ouvrir la page de connexion
            driver.get("http://localhost:4200/login"); // Remplacez par l'URL de votre application

            // Maximiser la fenêtre
            driver.manage().window().maximize();

            // Trouver les champs email et mot de passe
            WebElement emailField = driver.findElement(By.id("email"));
            WebElement passwordField = driver.findElement(By.id("password"));

            // Trouver le bouton "Se connecter"
            WebElement loginButton = driver.findElement(By.cssSelector("button[type='submit']"));

            // Test 1 : Vérification des messages d'erreur (champs vides)
            loginButton.click();
            WebElement emailError = driver.findElement(By.xpath("//p[contains(text(), \"L'email est requis\")]"));
            WebElement passwordError = driver.findElement(By.xpath("//p[contains(text(), \"Le mot de passe est requis\")]"));

            if (emailError.isDisplayed() && passwordError.isDisplayed()) {
                System.out.println("Test 1 réussi : Les messages d'erreur s'affichent correctement.");
            } else {
                System.out.println("Test 1 échoué : Les messages d'erreur ne s'affichent pas.");
            }

            // Test 2 : Connexion avec des données valides
            emailField.sendKeys("test@example.com");
            passwordField.sendKeys("password123");

            // Cliquer sur le bouton "Se connecter"
            loginButton.click();

            // Attendre que la page suivante ou un élément spécifique apparaisse après la connexion
            WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("dashboard")); // Remplacez "dashboard" par l'URL après connexion

            // Vérifier si l'utilisateur est redirigé correctement
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("dashboard")) {
                System.out.println("Test 2 réussi : Connexion réussie.");
            } else {
                System.out.println("Test 2 échoué : Redirection incorrecte après la connexion.");
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du test : " + e.getMessage());
        } finally {
            // Fermer le navigateur
            driver.quit();
        }
    }
}
