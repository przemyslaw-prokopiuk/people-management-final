-- Inserting students
INSERT INTO person (id, first_name, last_name, social_number, height, weight, email) VALUES (1, 'Karol', 'Kowalski', '95052103000', 170, 70, 'email@example.com');
INSERT INTO student (id, university_name, graduation_year, scholarship_amount) VALUES (1, 'Uniwersytet Warszawski', '2000-03-21', 919);

INSERT INTO person (id, first_name, last_name, social_number, height, weight, email) VALUES (2, 'Marek', 'Rynkowski', '95052103001', 222, 249, 'Yxgor@example.com');
INSERT INTO student (id, university_name, graduation_year, scholarship_amount) VALUES (2, 'Uniwersytet Warszawski', '2013-03-02', 768);

INSERT INTO person (id, first_name, last_name, social_number, height, weight, email) VALUES (3, 'Bartek', 'Komputer', '95052103002', 81, 97, 'Nehtf@example.com');
INSERT INTO student (id, university_name, graduation_year, scholarship_amount) VALUES (3, 'University Dupa', '1953-09-05', 989);

-- Inserting employees
INSERT INTO person (id, first_name, last_name, social_number, height, weight, email) VALUES (4, 'Karol', 'Kowalski', '95052103003', 171, 71, 'Jvezr@example.com');
INSERT INTO employee (id, position_name, start_date, salary) VALUES (4, 'Drukarz Frytkarz', '1972-03-25', 666);

INSERT INTO person (id, first_name, last_name, social_number, height, weight, email) VALUES (5, 'Marek', 'Rynkowski', '95052103004', 185, 232, 'Qvnfo@example.com');
INSERT INTO employee (id, position_name, start_date, salary) VALUES (5, 'Drukarz Frytkarz', '1980-02-17', 274);

INSERT INTO person (id, first_name, last_name, social_number, height, weight, email) VALUES (6, 'Bartek', 'Komputer', '95052103005', 136, 108, 'Lokxc@example.com');
INSERT INTO employee (id, position_name, start_date, salary) VALUES (6, 'Position Mzicx', '2011-12-07', 263);

-- Inserting pensioners
INSERT INTO person (id, first_name, last_name, social_number, height, weight, email) VALUES (7, 'Karol', 'Kowalski', '95052103006', 172, 72, 'Mzylx@example.com');
INSERT INTO pensioner (id, monthly_pension, total_years_of_work) VALUES (7, 1594, 70);

INSERT INTO person (id, first_name, last_name, social_number, height, weight, email) VALUES (8, 'Marek', 'Rynkowski', '95052103007', 137, 291, 'Jcvww@example.com');
INSERT INTO pensioner (id, monthly_pension, total_years_of_work) VALUES (8, 3719, 4);

INSERT INTO person (id, first_name, last_name, social_number, height, weight, email) VALUES (9, 'Bartek', 'Komputer', '95052103008', 149, 321, 'Bcqph@example.com');
INSERT INTO pensioner (id, monthly_pension, total_years_of_work) VALUES (9, 3414, 10);
