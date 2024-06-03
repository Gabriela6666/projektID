-- Drop existing tables, functions, views, and triggers if they exist
DROP TABLE IF EXISTS "public".oceny_uzytkownikow CASCADE;
DROP TABLE IF EXISTS "public".nagrody_pracownicy CASCADE;
DROP TABLE IF EXISTS "public".pochodzenie CASCADE;
DROP TABLE IF EXISTS "public".muzyka_pracownicy_tekst CASCADE;
DROP TABLE IF EXISTS "public".muzyka_pracownicy_dzwiek CASCADE;
DROP TABLE IF EXISTS "public".filmy_pracownicy CASCADE;
DROP TABLE IF EXISTS "public".filmy_panstwa CASCADE;
DROP TABLE IF EXISTS "public".filmy_jezyki CASCADE;
DROP TABLE IF EXISTS "public".filmy_gatunki CASCADE;
DROP TABLE IF EXISTS "public".uzytkownicy CASCADE;
DROP TABLE IF EXISTS "public".pracownicy CASCADE;
DROP TABLE IF EXISTS "public".panstwa CASCADE;
DROP TABLE IF EXISTS "public".oceny CASCADE;
DROP TABLE IF EXISTS "public".nagrody_filmy CASCADE;
DROP TABLE IF EXISTS "public".muzyka_filmy CASCADE;
DROP TABLE IF EXISTS "public".muzyka CASCADE;
DROP TABLE IF EXISTS "public".kategorie_filmy CASCADE;
DROP TABLE IF EXISTS "public".kategorie CASCADE;
DROP TABLE IF EXISTS "public".jezyki CASCADE;
DROP TABLE IF EXISTS "public".gatunki CASCADE;
DROP TABLE IF EXISTS "public".funkcje CASCADE;
DROP TABLE IF EXISTS "public".filmy CASCADE;

DROP FUNCTION IF EXISTS get_filmy_w_panstwie(varchar) CASCADE;
DROP FUNCTION IF EXISTS check_year_constraint() CASCADE;
DROP FUNCTION IF EXISTS wyzwalaczekategorie() CASCADE;
DROP FUNCTION IF EXISTS przyznaj_odznake() CASCADE;
DROP FUNCTION IF EXISTS top_100_best_films() CASCADE;
DROP FUNCTION IF EXISTS top_50_films_by_country(integer) CASCADE;

DROP VIEW IF EXISTS najsławniejsi_aktorzy CASCADE;
DROP VIEW IF EXISTS public.kalendarz_urodzin CASCADE;

DROP TRIGGER IF EXISTS trigger_check_year_constraint ON public.nagrody_filmy CASCADE;
DROP TRIGGER IF EXISTS trigger_wyzwalaczekategorie ON public.filmy_panstwa CASCADE;
DROP TRIGGER IF EXISTS trigger_przyznaj_odznake ON public.oceny_uzytkownikow CASCADE;

-- Create schema if it doesn't exist
CREATE SCHEMA IF NOT EXISTS "public";

-- Create tables
CREATE TABLE "public".filmy (
    id_filmu SERIAL,
    tytul varchar(100),
    rok_produkcji numeric(4) NOT NULL,
    CONSTRAINT pk_filmy PRIMARY KEY (id_filmu)
);

CREATE INDEX idx_filmy ON "public".filmy USING btree (tytul);

CREATE TABLE "public".funkcje (
    id_funkcji SERIAL,
    nazwa_funkcji varchar(30),
    CONSTRAINT pk_funkcje PRIMARY KEY (id_funkcji)
);

CREATE TABLE "public".gatunki (
    id_gatunku SERIAL NOT NULL,
    nazwa_gatunku varchar(50),
    CONSTRAINT pk_gatunki PRIMARY KEY (id_gatunku)
);

CREATE TABLE "public".jezyki (
    id_jezyka SERIAL NOT NULL,
    nazwa varchar(30),
    CONSTRAINT pk_jezyki PRIMARY KEY (id_jezyka)
);

CREATE TABLE "public".kategorie (
    id_kategorii SERIAL NOT NULL,
    nazwa_kategorii varchar(30) NOT NULL,
    CONSTRAINT pk_kategorie PRIMARY KEY (id_kategorii)
);

CREATE TABLE "public".kategorie_filmy (
    id_filmu integer NOT NULL,
    id_kategorii integer NOT NULL,
    CONSTRAINT pk_kategorie_filmy PRIMARY KEY (id_filmu, id_kategorii),
    CONSTRAINT fk_kategorie_filmy_filmy FOREIGN KEY (id_filmu) REFERENCES "public".filmy(id_filmu) ON DELETE CASCADE,
    CONSTRAINT fk_kategorie_filmy_kategorie FOREIGN KEY (id_kategorii) REFERENCES "public".kategorie(id_kategorii) ON DELETE CASCADE
);

CREATE TABLE "public".muzyka (
    id_utworu SERIAL NOT NULL,
    tytul_piosenki varchar(75),
    CONSTRAINT pk_muzyka PRIMARY KEY (id_utworu)
);

CREATE TABLE "public".muzyka_filmy (
    id_filmu integer NOT NULL,
    id_utworu integer NOT NULL,
    CONSTRAINT pk_muzyka_filmy PRIMARY KEY (id_utworu, id_filmu),
    CONSTRAINT fk_muzyka_filmy_muzyka FOREIGN KEY (id_utworu) REFERENCES "public".muzyka(id_utworu) ON DELETE CASCADE,
    CONSTRAINT fk_muzyka_filmy_filmy FOREIGN KEY (id_filmu) REFERENCES "public".filmy(id_filmu) ON DELETE CASCADE
);

CREATE TABLE "public".nagrody_filmy (
    id_nagrody SERIAL NOT NULL,
    id_filmu integer NOT NULL,
    nazwa_nagrody varchar(80),
    kto integer,
    rok_przyznania numeric(4,0),
    CONSTRAINT pk_nagrody_filmy PRIMARY KEY (id_nagrody),
    CONSTRAINT fk_nagrody_filmy FOREIGN KEY (id_filmu) REFERENCES "public".filmy(id_filmu) ON DELETE CASCADE
);

CREATE TABLE "public".oceny (
    id_oceny SERIAL NOT NULL,
    id_filmu integer NOT NULL,
    ocena_filmu numeric(2,0),
    CONSTRAINT pk_oceny PRIMARY KEY (id_oceny),
    CONSTRAINT fk_oceny_filmy FOREIGN KEY (id_filmu) REFERENCES "public".filmy(id_filmu) ON DELETE CASCADE
);

CREATE TABLE "public".panstwa (
    id_panstwa SERIAL NOT NULL,
    nazwa_panstwa varchar(50) NOT NULL,
    CONSTRAINT pk_panstwa PRIMARY KEY (id_panstwa)
);

CREATE TABLE "public".pracownicy (
    id_pracownika SERIAL,
    data_urodzenia date NOT NULL,
    data_smierci date,
    id_uzytkownika integer,
    CONSTRAINT pk_pracownicy PRIMARY KEY (id_pracownika)
);

CREATE TABLE "public".uzytkownicy (
    id_uzytkownika SERIAL NOT NULL,
    nazwa varchar(30) NOT NULL,
    imie varchar(50),
    nazwisko varchar(50),
    id_miejsce_zamieszkania integer,
    haslo varchar NOT NULL,
    CONSTRAINT pk_uzytkownicy PRIMARY KEY (id_uzytkownika),
    CONSTRAINT fk_uzytkownicy_panstwa FOREIGN KEY (id_miejsce_zamieszkania) REFERENCES "public".panstwa(id_panstwa) ON DELETE CASCADE
);

CREATE TABLE "public".filmy_gatunki (
    id_filmu integer NOT NULL,
    id_gatunku integer NOT NULL,
    CONSTRAINT pk_filmy_gatunki PRIMARY KEY (id_filmu, id_gatunku),
    CONSTRAINT fk_filmy_gatunki_filmy FOREIGN KEY (id_filmu) REFERENCES "public".filmy(id_filmu) ON DELETE CASCADE,
    CONSTRAINT fk_filmy_gatunki_gatunki FOREIGN KEY (id_gatunku) REFERENCES "public".gatunki(id_gatunku) ON DELETE CASCADE
);

CREATE TABLE "public".filmy_jezyki (
    id_fj SERIAL NOT NULL,
    id_filmu integer NOT NULL,
    id_jezyka integer,
    id_panstwa integer,
    CONSTRAINT pk_filmy_jezyki PRIMARY KEY (id_fj),
    CONSTRAINT fk_filmy_jezyki_panstwa FOREIGN KEY (id_panstwa) REFERENCES "public".panstwa(id_panstwa) ON DELETE CASCADE,
    CONSTRAINT fk_filmy_jezyki_jezyki FOREIGN KEY (id_jezyka) REFERENCES "public".jezyki(id_jezyka) ON DELETE CASCADE,
    CONSTRAINT fk_filmy_jezyki_filmy FOREIGN KEY (id_filmu) REFERENCES "public".filmy(id_filmu) ON DELETE CASCADE
);

CREATE TABLE "public".filmy_panstwa (
    id_filmu integer NOT NULL,
    id_panstwa integer NOT NULL,
    ograniczenie_wiekowe integer,
    data_premiery date,
    CONSTRAINT pk_filmy_panstwa PRIMARY KEY (id_filmu, id_panstwa),
    CONSTRAINT fk_filmy_panstwa_filmy FOREIGN KEY (id_filmu) REFERENCES "public".filmy(id_filmu) ON DELETE CASCADE,
    CONSTRAINT fk_filmy_panstwa_panstwa FOREIGN KEY (id_panstwa) REFERENCES "public".panstwa(id_panstwa) ON DELETE CASCADE
);

CREATE TABLE "public".filmy_pracownicy (
    id_filmy_pracownicy SERIAL NOT NULL,
    id_filmu integer,
    id_pracownika integer,
    id_funkcji integer,
    CONSTRAINT pk_filmy_pracownicy PRIMARY KEY (id_filmy_pracownicy),
    CONSTRAINT fk_filmy_pracownicy_funkcje FOREIGN KEY (id_funkcji) REFERENCES "public".funkcje(id_funkcji) ON DELETE CASCADE,
    CONSTRAINT fk_filmy_pracownicy_pracownicy FOREIGN KEY (id_pracownika) REFERENCES "public".pracownicy(id_pracownika) ON DELETE CASCADE,
    CONSTRAINT fk_filmy_pracownicy_filmy FOREIGN KEY (id_filmu) REFERENCES "public".filmy(id_filmu) ON DELETE CASCADE
);

CREATE TABLE "public".muzyka_pracownicy_dzwiek (
    id_utworu integer NOT NULL,
    id_pracownika integer,
    CONSTRAINT pk_muzyka_pracownicy_dzwiek PRIMARY KEY (id_utworu),
    CONSTRAINT fk_muzyka_pracownicy_dzwiek_pracownicy FOREIGN KEY (id_pracownika) REFERENCES "public".pracownicy(id_pracownika) ON DELETE CASCADE,
    CONSTRAINT fk_muzyka_pracownicy_dzwiek_muzyka FOREIGN KEY (id_utworu) REFERENCES "public".muzyka(id_utworu) ON DELETE CASCADE
);

CREATE TABLE "public".muzyka_pracownicy_tekst (
    id_utworu integer NOT NULL,
    id_pracownika integer,
    CONSTRAINT pk_muzyka_pracownicy_tekst PRIMARY KEY (id_utworu),
    CONSTRAINT fk_muzyka_pracownicy_tekst_pracownicy FOREIGN KEY (id_pracownika) REFERENCES "public".pracownicy(id_pracownika) ON DELETE CASCADE,
    CONSTRAINT fk_muzyka_pracownicy_tekst_muzyka FOREIGN KEY (id_utworu) REFERENCES "public".muzyka(id_utworu) ON DELETE CASCADE
);

CREATE TABLE "public".nagrody_pracownicy (
    id_nagrody_pracownicy SERIAL NOT NULL,
    id_nagrody_filmu integer NOT NULL,
    id_filmy_pracownicy integer,
    CONSTRAINT pk_nagrody_pracownicy PRIMARY KEY (id_nagrody_pracownicy),
    CONSTRAINT fk_nagrody_pracownicy_filmy_pracownicy FOREIGN KEY (id_filmy_pracownicy) REFERENCES "public".filmy_pracownicy(id_filmy_pracownicy) ON DELETE CASCADE,
    CONSTRAINT fk_nagrody_pracownicy_nagrody_filmy FOREIGN KEY (id_nagrody_filmu) REFERENCES "public".nagrody_filmy(id_nagrody) ON DELETE CASCADE
);

CREATE TABLE "public".oceny_uzytkownikow (
    id_uzytkownika integer NOT NULL,
    id_filmy_pracownicy integer NOT NULL,
    ocena numeric(2,0),
    data_oceny date NOT NULL,
    CONSTRAINT pk_oceny_uzytkownikow PRIMARY KEY (id_uzytkownika, id_filmy_pracownicy),
    CONSTRAINT fk_oceny_uzytkownikow_filmy_pracownicy FOREIGN KEY (id_filmy_pracownicy) REFERENCES "public".filmy_pracownicy(id_filmy_pracownicy) ON DELETE CASCADE,
    CONSTRAINT fk_oceny_uzytkownikow_uzytkownicy FOREIGN KEY (id_uzytkownika) REFERENCES "public".uzytkownicy(id_uzytkownika) ON DELETE CASCADE
);

CREATE TABLE "public".pochodzenie (
    id_pochodzenia SERIAL NOT NULL,
    id_pracownika integer,
    id_panstwa integer NOT NULL,
    CONSTRAINT pk_pochodzenie PRIMARY KEY (id_pochodzenia),
    CONSTRAINT fk_pochodzenie_panstwa FOREIGN KEY (id_panstwa) REFERENCES "public".panstwa(id_panstwa) ON DELETE CASCADE,
    CONSTRAINT fk_pochodzenie_pracownicy FOREIGN KEY (id_pracownika) REFERENCES "public".pracownicy(id_pracownika) ON DELETE CASCADE
);

-- Create or Replace Functions, Views, and Triggers

-- Ensure `kto` in `nagrody_filmy` is a valid `id_pracownika` for the given `id_filmu`
CREATE OR REPLACE FUNCTION validate_kto() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.kto IS NOT NULL AND NOT EXISTS (
        SELECT 1 FROM public.filmy_pracownicy
        WHERE id_filmu = NEW.id_filmu AND id_pracownika = NEW.kto
    ) THEN
        RETURN NULL;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_kto
BEFORE INSERT OR UPDATE ON public.nagrody_filmy
FOR EACH ROW
EXECUTE FUNCTION validate_kto();

CREATE OR REPLACE FUNCTION top_100_best_films()
RETURNS TABLE (
    id_filmu INTEGER,
    tytul VARCHAR(100),
    average_rating NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        f.id_filmu,
        f.tytul,
        AVG(ou.ocena) AS average_rating
    FROM 
        filmy f
    JOIN 
        oceny_uzytkownikow ou ON ou.id_filmy_pracownicy = f.id_filmu
    GROUP BY 
        f.id_filmu, f.tytul
    ORDER BY 
        average_rating DESC
    LIMIT 100;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION top_50_films_by_country(country_id INTEGER)
RETURNS TABLE (
    id_filmu INTEGER,
    tytul VARCHAR(100),
    average_rating NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        f.id_filmu,
        f.tytul,
        AVG(ou.ocena) AS average_rating
    FROM 
        filmy f
    JOIN 
        oceny_uzytkownikow ou ON ou.id_filmy_pracownicy = f.id_filmu
    JOIN 
        uzytkownicy u ON ou.id_uzytkownika = u.id_uzytkownika
    JOIN 
        panstwa p ON u.id_miejsce_zamieszkania = p.id_panstwa
    WHERE 
        p.id_panstwa = country_id
    GROUP BY 
        f.id_filmu, f.tytul
    ORDER BY 
        average_rating DESC
    LIMIT 50;
END;
$$ LANGUAGE plpgsql;

CREATE MATERIALIZED VIEW public.vw_pracownicy_uzytkownicy
AS
SELECT 
    p.id_pracownika,
    p.data_urodzenia,
    p.data_smierci,
    p.id_uzytkownika,
    u.imie,
    u.nazwisko
FROM 
    public.pracownicy p
JOIN 
    public.uzytkownicy u ON p.id_uzytkownika = u.id_uzytkownika;

CREATE INDEX idx_vw_pracownicy_nazwisko
ON public.vw_pracownicy_uzytkownicy(nazwisko);

CREATE OR REPLACE FUNCTION search_pracownicy_by_nazwisko(search_nazwisko VARCHAR)
RETURNS TABLE (
    id_pracownika INTEGER,
    data_urodzenia DATE,
    data_smierci DATE,
    id_uzytkownika INTEGER,
    imie VARCHAR(50),
    nazwisko VARCHAR(50)
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        id_pracownika,
        data_urodzenia,
        data_smierci,
        id_uzytkownika,
        imie,
        nazwisko
    FROM 
        public.vw_pracownicy_uzytkownicy
    WHERE 
        nazwisko = search_nazwisko;
END;
$$ LANGUAGE plpgsql;

REFRESH MATERIALIZED VIEW public.vw_pracownicy_uzytkownicy;

CREATE OR REPLACE FUNCTION top_50_films_by_category(kategoria_id INTEGER)
RETURNS TABLE (
    id_filmu INTEGER,
    tytul VARCHAR(100),
    average_rating NUMERIC
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        f.id_filmu,
        f.tytul,
        AVG(ou.ocena) AS average_rating
    FROM 
        filmy f
    JOIN 
        oceny_uzytkownikow ou ON ou.id_filmu = f.id_filmu
    JOIN 
        kategorie_filmy kf ON kf.id_filmu = f.id_filmu
    JOIN 
        kategorie k ON k.id_kategorii = kf.id_kategorii
    WHERE 
        k.id_kategorii = kategoria_id
    GROUP BY 
        f.id_filmu, f.tytul
    ORDER BY 
        average_rating DESC
    LIMIT 50;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_random_favorite_film_and_category(user_id INTEGER)
RETURNS TABLE (
    id_filmu INTEGER,
    tytul VARCHAR(100),
    id_kategorii INTEGER,
    nazwa_kategorii VARCHAR(30)
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        f.id_filmu,
        f.tytul,
        k.id_kategorii,
        k.nazwa_kategorii
    FROM 
        filmy f
    JOIN 
        oceny_uzytkownikow ou ON ou.id_filmu = f.id_filmu
    JOIN 
        kategorie_filmy kf ON kf.id_filmu = f.id_filmu
    JOIN 
        kategorie k ON k.id_kategorii = kf.id_kategorii
    WHERE 
        ou.id_uzytkownika = user_id
        AND ou.ocena >= 8
    ORDER BY 
        RANDOM()
    LIMIT 1;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION suggest_three_best_films(user_id INTEGER)
RETURNS TABLE (
    id_filmu INTEGER,
    tytul VARCHAR(100),
    average_rating NUMERIC
) AS $$
DECLARE
    selected_film RECORD;
BEGIN
    -- Get a random favorite film and its category for the user
    SELECT * INTO selected_film
    FROM get_random_favorite_film_and_category(user_id);

    -- If no film is found, return an empty result set
    IF NOT FOUND THEN
        RETURN;
    END IF;

    -- Get the top three films from the selected category
    RETURN QUERY
    SELECT * FROM top_50_films_by_category(selected_film.id_kategorii)
    LIMIT 3;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION get_filmy_w_panstwie(kraj varchar)
RETURNS TABLE (
    id_filmu integer,
    tytul varchar,
    rok_produkcji numeric(4),
    ograniczenie_wiekowe integer,
    data_premiery date
) AS $$
BEGIN
    RETURN QUERY
    SELECT 
        f.id_filmu,
        f.tytul,
        f.rok_produkcji,
        fp.ograniczenie_wiekowe,
        fp.data_premiery
    FROM 
        "public".filmy f
    JOIN 
        "public".filmy_panstwa fp ON f.id_filmu = fp.id_filmu
    JOIN 
        "public".panstwa p ON fp.id_panstwa = p.id_panstwa
    WHERE 
        p.nazwa_panstwa = kraj;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE VIEW najsławniejsi_aktorzy AS
SELECT 
    p.id_pracownika AS id,
    u.imie,
    u.nazwisko,
    COUNT(DISTINCT np.id_nagrody_pracownicy) AS liczba_nagrod,
    COALESCE(AVG(ou.ocena), 0) AS srednia_ocena
FROM 
    "public".pracownicy p
LEFT JOIN 
    "public".filmy_pracownicy fp ON p.id_pracownika = fp.id_pracownika
LEFT JOIN 
    "public".nagrody_pracownicy np ON fp.id_filmy_pracownicy = np.id_filmy_pracownicy
LEFT JOIN 
    "public".oceny_uzytkownikow ou ON np.id_filmy_pracownicy = ou.id_filmy_pracownicy
LEFT JOIN
    "public".uzytkownicy u ON p.id_uzytkownika = u.id_uzytkownika
GROUP BY 
    p.id_pracownika, u.imie, u.nazwisko
ORDER BY 
    liczba_nagrod DESC, srednia_ocena DESC;

CREATE OR REPLACE VIEW public.kalendarz_urodzin AS
SELECT
    EXTRACT(MONTH FROM p.data_urodzenia) AS miesiac_urodzenia,
    EXTRACT(DAY FROM p.data_urodzenia) AS dzien_urodzenia,
    u.imie AS imie_aktora,
    u.nazwisko AS nazwisko_aktora
FROM
    public.pracownicy p
JOIN
    public.uzytkownicy u ON p.id_uzytkownika = u.id_uzytkownika
WHERE
    p.data_urodzenia IS NOT NULL;

CREATE OR REPLACE FUNCTION check_year_constraint()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.rok_przyznania <= (SELECT rok_produkcji FROM public.filmy WHERE id_filmu = NEW.id_filmu) THEN
        RETURN NULL;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_check_year_constraint
BEFORE INSERT OR UPDATE ON public.nagrody_filmy
FOR EACH ROW
EXECUTE FUNCTION check_year_constraint();

CREATE OR REPLACE FUNCTION wyzwalaczekategorie()
RETURNS TRIGGER AS $$
BEGIN
    IF (NEW.nazwa_gatunku = 'Horror' AND ( NEW.ograniczenie_wiekowe < 16)) THEN
        RAISE EXCEPTION 'Filmy Horror muszą mieć ograniczenie wiekowe co najmniej 16 lat';
    ELSIF (NEW.nazwa_gatunku = 'Smutek' AND NEW.nazwa_kategorii = 'Komedia') THEN
        RAISE EXCEPTION 'Filmy Smutek nie mogą należeć do kategorii Komedia';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_wyzwalaczekategorie
BEFORE INSERT OR UPDATE ON public.filmy_panstwa
FOR EACH ROW
EXECUTE FUNCTION wyzwalaczekategorie();

CREATE VIEW public.rank_najaktywniejszych_uzytkownikow AS
SELECT
    u.id_uzytkownika,
    u.imie,
    u.nazwisko,
    COUNT(ou.id_uzytkownika) AS ilosc_opinii
FROM
    public.uzytkownicy u
JOIN
    public.oceny_uzytkownikow ou ON u.id_uzytkownika = ou.id_uzytkownika
GROUP BY
    u.id_uzytkownika, u.imie, u.nazwisko
ORDER BY
    ilosc_opinii DESC;

CREATE OR REPLACE FUNCTION przyznaj_odznake()
RETURNS TRIGGER AS $$
DECLARE
    najlepszy_uzytkownik INTEGER;
    drugi_najaktywniejszy INTEGER;
    trzeci_najaktywniejszy INTEGER;
BEGIN
    SELECT id_uzytkownika INTO STRICT najlepszy_uzytkownik
    FROM public.rank_najaktywniejszych_uzytkownikow
    LIMIT 1;

    SELECT id_uzytkownika INTO STRICT drugi_najaktywniejszy
    FROM public.rank_najaktywniejszych_uzytkownikow
    OFFSET 1
    LIMIT 1;

    SELECT id_uzytkownika INTO STRICT trzeci_najaktywniejszy
    FROM public.rank_najaktywniejszych_uzytkownikow
    OFFSET 2
    LIMIT 1;

    UPDATE public.uzytkownicy
    SET odznaka = 'Złota'
    WHERE id_uzytkownika = najlepszy_uzytkownik;

    UPDATE public.uzytkownicy
    SET odznaka = 'Srebrna'
    WHERE id_uzytkownika = drugi_najaktywniejszy;

    UPDATE public.uzytkownicy
    SET odznaka = 'Brązowa'
    WHERE id_uzytkownika = trzeci_najaktywniejszy;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_przyznaj_odznake
AFTER INSERT ON public.oceny_uzytkownikow
FOR EACH ROW
EXECUTE FUNCTION przyznaj_odznake();

-- Generowanie danych

-- Generate data for tables
DO $$
BEGIN
    -- Generate data for filmy
    FOR i IN 1..1500 LOOP
        INSERT INTO public.filmy (tytul, rok_produkcji)
        VALUES ('Film ' || i, 2000 + (i % 20))
        ON CONFLICT DO NOTHING;
    END LOOP;

    -- Generate data for pracownicy
    FOR i IN 1..500 LOOP
        INSERT INTO public.pracownicy (data_urodzenia, data_smierci, id_uzytkownika)
        VALUES (
            date '1950-01-01' + (i % 3650),
            CASE WHEN i % 2 = 0 THEN date '2000-01-01' + (i % 3650) ELSE NULL END,
            (i % 100) + 1
        )
        ON CONFLICT DO NOTHING;
    END LOOP;

    -- Generate data for gatunki
    FOR i IN 1..50 LOOP
        INSERT INTO public.gatunki (id_gatunku, nazwa_gatunku)
        VALUES (i, 'Gatunek ' || i)
        ON CONFLICT (id_gatunku) DO NOTHING;
    END LOOP;

    -- Generate data for jezyki
    FOR i IN 1..50 LOOP
        INSERT INTO public.jezyki (id_jezyka, nazwa)
        VALUES (i, 'Jezyk ' || i)
        ON CONFLICT (id_jezyka) DO NOTHING;
    END LOOP;

    -- Generate data for panstwa
    FOR i IN 1..50 LOOP
        INSERT INTO public.panstwa (id_panstwa, nazwa_panstwa)
        VALUES (i, 'Panstwo ' || i)
        ON CONFLICT (id_panstwa) DO NOTHING;
    END LOOP;

    -- Generate data for kategorie
    FOR i IN 1..20 LOOP
        INSERT INTO public.kategorie (id_kategorii, nazwa_kategorii)
        VALUES (i, 'Kategoria ' || i)
        ON CONFLICT (id_kategorii) DO NOTHING;
    END LOOP;

    -- Generate data for muzyka
    FOR i IN 1..100 LOOP
        INSERT INTO public.muzyka (id_utworu, tytul_piosenki)
        VALUES (i, 'Piosenka ' || i)
        ON CONFLICT (id_utworu) DO NOTHING;
    END LOOP;

    -- Generate data for oceny
    FOR i IN 1..500 LOOP
        INSERT INTO public.oceny (id_filmu, ocena_filmu)
        VALUES ((i % 1500) + 1, (i % 10) + 1)
        ON CONFLICT DO NOTHING;
    END LOOP;

    -- Generate data for uzytkownicy
    FOR i IN 1..100 LOOP
        INSERT INTO public.uzytkownicy (nazwa, imie, nazwisko, id_miejsce_zamieszkania, haslo)
        VALUES (
            'user' || i,
            'Imie ' || i,
            'Nazwisko ' || i,
            (i % 50) + 1,
            --simple_encrypt('password' || i, i)
            'password' || i
        )
        ON CONFLICT DO NOTHING;
    END LOOP;
END $$;

-- Generowanie danych dla tabeli funkcje
DO $$
BEGIN
    FOR i IN 1..10 LOOP
        INSERT INTO public.funkcje (id_funkcji, nazwa_funkcji)
        VALUES (i, 'Funkcja ' || i)
        ON CONFLICT (id_funkcji) DO NOTHING;
    END LOOP;
END $$;

-- Generowanie danych dla tabeli filmy_pracownicy
DO $$
BEGIN
    FOR i IN 1..5000 LOOP
        INSERT INTO public.filmy_pracownicy (id_filmu, id_pracownika, id_funkcji)
        VALUES ((i % 1500) + 1, (i % 500) + 1, (i % 10) + 1)
        ON CONFLICT DO NOTHING;
    END LOOP;
END $$;

-- Generowanie danych dla tabeli nagrody_filmy z warunkiem
DO $$
DECLARE
    pracownik_id INTEGER;
BEGIN
    FOR i IN 1..100 LOOP
        SELECT id_pracownika INTO pracownik_id
        FROM public.filmy_pracownicy
        WHERE id_filmu = (i % 1500) + 1
        ORDER BY random()
        LIMIT 1;

        INSERT INTO public.nagrody_filmy (id_nagrody, id_filmu, nazwa_nagrody, kto, rok_przyznania)
        VALUES (i, (i % 1500) + 1, 'Nagroda ' || i, pracownik_id, 2000 + (i % 24))
        ON CONFLICT (id_nagrody) DO NOTHING;
    END LOOP;
END $$;
