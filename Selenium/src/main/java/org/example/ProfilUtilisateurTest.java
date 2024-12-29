package org.example;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.Duration;
import java.util.List;

public class ProfilUtilisateurTest {

    public static void main(String[] args) {
        // Spécifier le chemin de ChromeDriver
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");

        // Créer une instance de ChromeOptions pour gérer les options de Chrome
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");  // Exécution en mode headless (sans interface graphique)

        // Initialiser WebDriver
        WebDriver driver = new ChromeDriver(options);

        try {
            // Ouvrir la page du profil utilisateur
            driver.get("http://localhost:4200/profil");

            // Attendre que la page se charge
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Vérifier que le titre "Profil Utilisateur" est présent
            WebElement profileTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("h2")));
            System.out.println("Profil Utilisateur : " + profileTitle.getText());

            // Vérifier que le bouton "Modifier mes informations" est présent
            WebElement editButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Modifier mes informations')]")));
            System.out.println("Bouton Modifier : " + editButton.getText());

            // Vérifier la présence de la liste des tâches assignées
            List<WebElement> tasks = driver.findElements(By.cssSelector("table tbody tr"));
            System.out.println("Nombre de tâches assignées : " + tasks.size());

            // Vérifier le statut d'une tâche
            WebElement taskStatus = tasks.get(0).findElement(By.cssSelector("td:nth-child(3) span"));
            System.out.println("Statut de la première tâche : " + taskStatus.getText());

            // Fermer le navigateur
        } finally {
            driver.quit();
        }
    }
}
