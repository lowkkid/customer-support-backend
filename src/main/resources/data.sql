-- BUG messages (5)
INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Alice Johnson', 'The app keeps crashing when I try to upload a file. It happens every time with PDFs larger than 5MB.', 'BUG', 'HIGH', 'UNRESOLVED', '2026-02-14 09:15:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Tom Rivera', 'Login page shows a blank white screen on Safari. I can''t access my account at all.', 'BUG', 'HIGH', 'UNRESOLVED', '2026-02-14 10:30:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Sarah Chen', 'The search function returns no results even when I search for items I know exist in my inventory.', 'BUG', 'MEDIUM', 'UNRESOLVED', '2026-02-14 11:00:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Mike O''Brien', 'Dashboard charts are not loading properly — I see a spinning loader that never finishes.', 'BUG', 'MEDIUM', 'UNRESOLVED', '2026-02-13 16:45:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Priya Patel', 'Notification emails are being sent twice for every event. Started happening since last week.', 'BUG', 'MEDIUM', 'UNRESOLVED', '2026-02-13 14:20:00');

-- BILLING messages (5)
INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('James Wilson', 'My invoice is wrong for last month — I was charged twice for the same subscription.', 'BILLING', 'HIGH', 'UNRESOLVED', '2026-02-14 08:00:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Emma Davis', 'I cancelled my plan two weeks ago but I was still charged this month. Please refund.', 'BILLING', 'HIGH', 'UNRESOLVED', '2026-02-14 09:45:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Carlos Mendez', 'Can I get a receipt for my last three payments? I need them for my company expense report.', 'BILLING', 'LOW', 'UNRESOLVED', '2026-02-13 13:30:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Linda Kim', 'I upgraded to the Pro plan but my account still shows the Basic features. Billing shows Pro price though.', 'BILLING', 'MEDIUM', 'UNRESOLVED', '2026-02-14 12:10:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('David Brown', 'What payment methods do you accept? I want to switch from credit card to bank transfer.', 'BILLING', 'LOW', 'UNRESOLVED', '2026-02-12 10:00:00');

-- FEATURE_REQUEST messages (4)
INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Nina Kowalski', 'Can you add support for dark mode in the next release? It would really help when working at night.', 'FEATURE_REQUEST', 'LOW', 'UNRESOLVED', '2026-02-13 18:00:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Ryan Thompson', 'It would be great to have a Slack integration so we get notified about updates directly in our channels.', 'FEATURE_REQUEST', 'MEDIUM', 'UNRESOLVED', '2026-02-14 11:30:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Olivia Martinez', 'Please add the ability to export reports as CSV or Excel. Right now I have to copy-paste everything manually.', 'FEATURE_REQUEST', 'MEDIUM', 'UNRESOLVED', '2026-02-13 15:15:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Kevin Lee', 'Would love to see a mobile app version. I often need to check things on the go.', 'FEATURE_REQUEST', 'LOW', 'UNRESOLVED', '2026-02-12 09:00:00');

-- GENERAL messages (4)
INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Hannah Scott', 'How do I reset my password? I can''t find the option anywhere in the settings.', 'GENERAL', 'LOW', 'UNRESOLVED', '2026-02-14 07:30:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Alex Turner', 'Just wanted to say your support team was amazing last time. Keep up the great work!', 'GENERAL', 'LOW', 'UNRESOLVED', '2026-02-13 20:00:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Maria Garcia', 'What are your support hours? I tried calling yesterday evening but no one picked up.', 'GENERAL', 'LOW', 'UNRESOLVED', '2026-02-12 17:45:00');

INSERT INTO support_message (customer_name, message_body, category, priority, status, created_at)
VALUES ('Ben Walker', 'Is there any documentation or user guide available? I''m new to the platform and feeling a bit lost.', 'GENERAL', 'LOW', 'UNRESOLVED', '2026-02-13 10:15:00');
