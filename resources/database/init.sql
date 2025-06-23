CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS unaccent;

CREATE TABLE articles
(
    id            SERIAL PRIMARY KEY,
    title         TEXT NOT NULL,
    subtitle      TEXT,
    content       TEXT NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    search_vector tsvector GENERATED ALWAYS AS (
        setweight(to_tsvector('english', coalesce(title, '')), 'A') ||
        setweight(to_tsvector('english', coalesce(subtitle, '')), 'B') ||
        setweight(to_tsvector('english', coalesce(content, '')), 'C')
        ) STORED
);

CREATE INDEX articles_search_idx ON articles USING GIN (search_vector);

-- Insert test data for full-text search testing
INSERT INTO articles (title, subtitle, content) VALUES 
('The Future of Artificial Intelligence', 'How AI is Changing Our World', 'Artificial intelligence continues to evolve at a rapid pace. From self-driving cars to advanced medical diagnostics, AI technologies are revolutionizing industries across the globe. Researchers are now focusing on developing more ethical and transparent AI systems that can work alongside humans effectively.');

INSERT INTO articles (title, subtitle, content) VALUES 
('Climate Change: A Global Crisis', 'Understanding the Impact of Global Warming', 'Climate change remains one of the most pressing challenges of our time. Rising temperatures, extreme weather events, and melting ice caps are just some of the observable effects. Scientists warn that without immediate action to reduce carbon emissions, we face potentially catastrophic consequences for ecosystems and human societies alike.');

INSERT INTO articles (title, subtitle, content) VALUES 
('The Rise of Remote Work', 'How the Pandemic Changed the Workplace Forever', 'The COVID-19 pandemic accelerated the adoption of remote work across industries. Companies that never considered flexible work arrangements suddenly found themselves managing entirely distributed teams. This shift has prompted organizations to rethink office spaces, collaboration tools, and work-life balance policies.');

INSERT INTO articles (title, subtitle, content) VALUES 
('Blockchain Beyond Cryptocurrency', 'Exploring Real-World Applications', 'While blockchain technology is most commonly associated with Bitcoin and other cryptocurrencies, its potential applications extend far beyond digital currency. From supply chain management to voting systems, blockchain''s decentralized and transparent nature offers solutions to longstanding problems in various sectors.');

INSERT INTO articles (title, subtitle, content) VALUES 
('The Science of Sleep', 'Why Rest is Critical for Health and Performance', 'Sleep researchers have discovered that quality rest plays a vital role in cognitive function, immune health, and emotional wellbeing. Despite this knowledge, sleep deprivation remains common in modern society. New studies suggest that consistent sleep patterns may be as important as total sleep duration.');

INSERT INTO articles (title, subtitle, content) VALUES 
('Sustainable Agriculture', 'Feeding the World Without Destroying It', 'As global population continues to grow, agricultural systems face the challenge of increasing food production while reducing environmental impact. Sustainable farming practices, including crop rotation, reduced tillage, and precision agriculture, offer promising solutions to balance productivity with ecological responsibility.');

INSERT INTO articles (title, content) VALUES 
('The Psychology of Decision Making', 'Cognitive biases affect our everyday choices in ways we rarely notice. From confirmation bias to the sunk cost fallacy, these mental shortcuts can lead to irrational decisions. Understanding these biases is the first step toward making more objective and effective choices in both personal and professional contexts.');

INSERT INTO articles (title, subtitle, content) VALUES 
('Space Exploration in the 21st Century', 'The New Space Race', 'Private companies have transformed space exploration, bringing costs down and accelerating innovation. SpaceX, Blue Origin, and others are developing technologies for everything from satellite internet to Mars colonization. Meanwhile, national space agencies are focusing on scientific missions and international collaboration.');

INSERT INTO articles (title, subtitle, content) VALUES 
('The Evolution of Social Media', 'From Connection to Polarization', 'Social media platforms have transformed from simple communication tools to complex ecosystems that shape public discourse. While they enable unprecedented connectivity, concerns about privacy, misinformation, and algorithmic bias have prompted calls for greater regulation and more thoughtful design.');

INSERT INTO articles (title, content) VALUES 
('Advances in Renewable Energy', 'Solar and wind power have reached price parity with fossil fuels in many markets, accelerating the transition to clean energy. Innovations in energy storage, particularly battery technology, are addressing intermittency issues. Meanwhile, emerging technologies like green hydrogen offer solutions for hard-to-decarbonize sectors.');

INSERT INTO articles (title, subtitle, content) VALUES 
('The Microbiome Revolution', 'How Gut Bacteria Influence Health', 'Research into the human microbiome has revealed the crucial role that gut bacteria play in digestion, immunity, and even mental health. Scientists are exploring how dietary choices, antibiotics, and lifestyle factors shape our microbial communities, with implications for treating conditions from inflammatory bowel disease to depression.');

INSERT INTO articles (title, content) VALUES 
('Digital Privacy in the Surveillance Age', 'As data collection becomes increasingly pervasive, concerns about privacy have moved to the forefront of public consciousness. Encryption, anonymization tools, and privacy-focused legislation are developing in response to these concerns. The tension between security, convenience, and privacy rights continues to shape digital policy.');

INSERT INTO articles (title, subtitle, content) VALUES 
('The Future of Transportation', 'Beyond Electric Vehicles', 'While electric vehicles represent a significant step toward sustainable transportation, innovations continue to emerge. Hyperloop systems, advanced public transit, and urban planning that reduces the need for cars altogether are reshaping how we think about mobility. The goal is not just cleaner vehicles, but smarter transportation ecosystems.');

INSERT INTO articles (title, content) VALUES 
('Quantum Computing Explained', 'Quantum computers leverage the principles of quantum mechanics to perform calculations impossible for traditional computers. While still in early stages, these machines promise breakthroughs in fields from cryptography to drug discovery. Major tech companies and governments are investing heavily in quantum research and development.');

INSERT INTO articles (title, subtitle, content) VALUES 
('The Neuroscience of Habit Formation', 'Rewiring Your Brain for Success', 'Habits form through a neurological loop consisting of cue, routine, and reward. Understanding this process allows for more effective behavior change strategies. Recent research suggests that focusing on the cue and reward, rather than willpower alone, leads to more sustainable habit transformation.');

INSERT INTO articles (title, content) VALUES 
('Ocean Conservation Challenges', 'Marine ecosystems face unprecedented threats from plastic pollution, overfishing, and acidification due to climate change. Conservation efforts include marine protected areas, sustainable fishing practices, and innovative cleanup technologies. Protecting ocean biodiversity is crucial not only for marine life but for the billions of humans who depend on healthy seas.');

INSERT INTO articles (title, subtitle, content) VALUES 
('The Economics of Happiness', 'Why Wealth Doesn''t Always Equal Wellbeing', 'Economists are increasingly studying happiness and life satisfaction as measures of societal progress. Research shows that beyond meeting basic needs, additional income produces diminishing returns in wellbeing. Factors like social connections, meaningful work, and time affluence may contribute more to happiness than financial wealth alone.');

INSERT INTO articles (title, content) VALUES 
('Cybersecurity in the IoT Era', 'As billions of devices connect to the internet, security vulnerabilities multiply exponentially. The Internet of Things creates new attack surfaces for malicious actors, from smart home devices to industrial sensors. Developing secure-by-design protocols and improving consumer awareness are critical challenges for the cybersecurity community.');

INSERT INTO articles (title, subtitle, content) VALUES 
('The Future of Education', 'Adapting Learning for the AI Age', 'Educational systems designed for the industrial era are struggling to prepare students for a rapidly changing world. Progressive approaches emphasize critical thinking, creativity, and adaptability over memorization. Technology offers personalized learning opportunities, while human teachers focus increasingly on mentorship and social-emotional development.');

INSERT INTO articles (title, subtitle, content) VALUES 
('Genetic Engineering Ethics', 'The CRISPR Revolution and Its Implications', 'CRISPR gene editing technology has made precise genetic modifications more accessible than ever before. This raises profound ethical questions about human enhancement, designer babies, and the boundaries of medical intervention. Scientists, ethicists, and policymakers are working to establish frameworks that balance innovation with responsible governance.');
