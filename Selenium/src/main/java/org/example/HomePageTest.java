package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePageTest {
    public static void main(String[] args) {
        // Configure le chemin vers le ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

        // Initialise le WebDriver
        WebDriver driver = new ChromeDriver();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        try {
            // Ouvre la page d'accueil
            driver.get("http://localhost:4200/home");
            driver.manage().window().maximize();

            // ** Scénario 1 : Rechercher un projet **
            WebElement searchInput = driver.findElement(By.id("searchQuery"));
            searchInput.sendKeys("Projet Test");
            WebElement firstProject = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".text-2xl")));
            if (firstProject.getText().toLowerCase().contains("projet test")) {
                System.out.println("Test recherche projet : Passé");
            }

            // ** Scénario 2 : Ajouter un projet **
            WebElement addButton = driver.findElement(By.cssSelector("button.ml-4"));
            addButton.click();
            WebElement addProjectDialog = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".mat-dialog-container")));
            if (addProjectDialog.isDisplayed()) {
                System.out.println("Test ajout projet : Passé");
            }

            // ** Scénario 3 : Afficher les détails d'un projet **
            WebElement projectCard = driver.findElement(By.cssSelector(".p-6"));
            projectCard.click();
            wait.until(ExpectedConditions.urlContains("/project-detail"));
            System.out.println("Test navigation vers détails projet : Passé");

        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Ferme le navigateur
            driver.quit();
        }
    }
}
