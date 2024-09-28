const express = require('express');
const axios = require('axios');
const cheerio = require('cheerio');
const app = express();
const port = 3000;

app.use(express.json());

app.post('/skeletonize', async (req, res) => {
    const { url } = req.body;
    try {
        const response = await axios.get(url);
        const html = response.data;
        const $ = cheerio.load(html);
        const cssLinks = $('link[rel="stylesheet"]').map((i, el) => $(el).attr('href')).get();
        const cssPromises = cssLinks.map(link => axios.get(link.startsWith('http') ? link : `${url}${link}`));
        const cssResponses = await Promise.all(cssPromises);
        const css = cssResponses.map(response => response.data).join('\n');
        res.json({ html, css });
    } catch (error) {
        res.status(500).send('Error fetching the website');
    }
});

app.listen(port, () => {
    console.log(`Server running at http://localhost:${port}`);
});
