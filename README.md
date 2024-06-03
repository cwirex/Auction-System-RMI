# Programowanie Współbieżna i Rozproszone (2024)
## Program Zaliczeniowy

**Mateusz Oćwieja, 303814**

### Aukcje Internetowe - Krótka Instrukcja i Opis

Serwis rozpoczyna działanie. Użytkownik, posiadający specjalny klucz, może uzyskać dostęp do konta Administratora. Administrator tworzy przedmioty aukcyjne, aukcje i użytkowników. Użytkownicy mogą się zalogować na dedykowane konta. Po zalogowaniu, powinni doładować swój portfel nowymi środkami. Następnie, mogą przeglądać aukcje i wystawione na nich przedmioty. Użytkownicy dołączają do aukcji i licytują, aż się skończy czas. Serwer wyświetla informacje o aukcji (na stronie lub w konsoli): opis przedmiotu, aktualna cena, lista licytujących. Jeśli ktoś chce licytować to podaje swoją maksymalną cenę, a system umieszcza go na liście licytujących. Jeśli jego oferta jest najwyższa, zostaje to odnotowane i staje się liderem. Przy zamykaniu aukcji, przedmiot na niej licytowany, trafia do oferenta, który zaoferował najwyższą kwotę.

### Dokumentacja:
[Docs of Auction Service Library (Service Interface)](docs)

### Struktura Logiczna Aplikacji
![Alt Text](LogicalStructure.png)

### Interfejs serwisu aukcyjnego zapewnia:
#### Funkcje Administracyjne (Admin)
- Tworzenie Użytkowników: Admin może tworzyć nowych użytkowników, podając ich login i hasło.
- Tworzenie Przedmiotów: Admin może tworzyć nowe przedmioty aukcyjne, definiując ich nazwę oraz początkową wartość.
- Tworzenie Aukcji: Admin może tworzyć aukcje dla istniejących przedmiotów, ustalając czas rozpoczęcia i zakończenia aukcji.
- Zarządzanie Danymi: Admin ma dostęp do listy wszystkich użytkowników, przedmiotów oraz aukcji.

#### Funkcje Aukcji (Auction)
- Licytacja: Użytkownicy mogą składać oferty na przedmioty wystawione na aukcję.
- Powiadomienia: System może wysyłać powiadomienia do wszystkich uczestników aukcji.
- Śledzenie Ofert: Aukcja przechowuje informacje o najwyższej ofercie oraz użytkowniku, który ją złożył.
- Status Aukcji: Aukcja posiada różne statusy (nowa, otwarta, zamknięta), które odzwierciedlają jej bieżący stan.

#### Funkcje Użytkowników (User)
- Logowanie: Użytkownicy mogą logować się do systemu, podając swój login i hasło.
- Składanie Ofert: Użytkownicy mogą składać oferty w aukcjach, zarówno bezpośrednio, jak i poprzez podanie ID aukcji.
- Zarządzanie Kredytami: Użytkownicy mogą doładowywać swoje konto kredytowe przy pomocy specjalnych kodów BL1K.
- Odbiór Przedmiotów: Po zakończeniu aukcji, użytkownicy mogą odbierać wylicytowane przedmioty (są im przekazywane automatycznie).

#### Ogólne Funkcje Serwisu (Service)
- Lista Aukcji: Wszyscy mogą przeglądać listę dostępnych aukcji.
- Interfejs Logowania: Zapewnia interfejs do logowania, udostępniając konto Administratora lub Użytkownika.
