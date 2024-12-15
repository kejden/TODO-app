# Temat: Opis zadania: System zarządzania zadaniami z wykorzystaniem Spring Boot

## Cel projektu
Stwórz aplikację internetową do zarządzania zadaniami. Użytkownicy będą mogli tworzyć, edytować i usuwać zadania, które są przypisane do nich. Każde zadanie może należeć do określonej kategorii. Aplikacja powinna umożliwiać:
- Logowanie użytkownika.
- Opcjonalnie rejestrację nowych użytkowników.
- Tworzenie i zarządzanie kategoriami (każdy użytkownik ma swoje unikalne kategorie).
- Tworzenie i zarządzanie zadaniami oraz ich statusem.

## Wymagania funkcjonalne:
### Moduł zarządzania użytkownikami:
- Implementacja logowania z wykorzystaniem Spring Security.
- Powiązanie danych (kategorii i zadań) z zalogowanym użytkownikiem.
- Własny formularz logowania.
- Możliwość wylogowania użytkownika.
- Opcjonalnie rejestrację nowych użytkowników.
### Moduł zarządzania kategoriami:
- Użytkownicy mogą tworzyć kategorie i zarządzać nimi.
- Kategorie są unikalne dla danego użytkownika (ta sama nazwa kategorii może istnieć u różnych użytkowników, ale jeden użytkownik nie może posiadać dwóch kategorii o tej samej nazwie).
- Każda kategoria ma nazwę i jest przypisana do użytkownika.
### Moduł zarządzania zadaniami:
- Użytkownicy mogą tworzyć zadania, edytować je, zmieniać ich status, usuwać i wyświetlać.
- Zadanie zawiera: Tytuł, Opis, Status(NEW, IN_PROGRESS, COMPLETED), Kategorię
- Zadania są przypisane do użytkownika.
### Widoki aplikacji
- Formularze i tabele do zarządzania kategoriami i zadaniami.
- Widoki:
  - Lista zadań.
  - Formularz tworzenia nowego zadania.
  - Formularz edycji zadania.
  - Lista kategorii.
  - Formularz tworzenia kategorii.
  - Formularz edycji kategorii.

## Wymagania techniczne:
1. Framework: Spring Boot 3.
2. Baza danych: H2 z wykorzystaniem JPA i Hibernate, oraz Liquibase z formatem XML do tworzenia
danych inicjalnych.
3. Interfejs użytkownika: Thymeleaf z Bootstrapem.
4. Bezpieczeństwo: Spring Security.
5. Walidacja: Walidacja danych na poziomie serwisów oraz encji
## Przykładowe konta 
1. Login: user1@example.com Hasło: password
2. Login: user2@example.com Hasło: password
## Przykładowe Screeny
![image](https://github.com/user-attachments/assets/be8194ed-bbf6-4b90-92ac-37aeec50c264)
![image](https://github.com/user-attachments/assets/06ce7bbd-9c14-40bb-85b0-223a81769dc5)
![image](https://github.com/user-attachments/assets/17bf039b-0b76-4dc6-b9c4-2fd18e705858)
![image](https://github.com/user-attachments/assets/3ed56075-c3be-4c6f-bdf3-e51e19a06449)
![image](https://github.com/user-attachments/assets/362c5f0b-3d8b-482e-aa29-45607c721da5)
![image](https://github.com/user-attachments/assets/d3aa9e2c-511f-474c-9761-ba23b28833a4)
![image](https://github.com/user-attachments/assets/662b48e2-fb6f-4cc3-9e90-d58044349a22)
![image](https://github.com/user-attachments/assets/c57d253c-d13f-41b1-a34c-608eb664cffa)
![image](https://github.com/user-attachments/assets/d7d7d9ab-461d-40a0-a73d-2022a0c04237)
![image](https://github.com/user-attachments/assets/48bf337d-3c2e-4406-9ed9-e29eb7e1a807)
![image](https://github.com/user-attachments/assets/e91a6588-d5dc-4807-b862-55a45eb41e56)
