const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const { SMTPServer } = require('smtp-server');
const { simpleParser } = require('mailparser');
const { v4: uuidv4 } = require('uuid');

const app = express();
const PORT = process.env.PORT || 3000;
const SMTP_PORT = process.env.SMTP_PORT || 1025;

// Middleware
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));

// In-memory storage for emails
let emails = [];
let emailStats = {
  totalReceived: 0,
  totalSent: 0,
  lastReceived: null
};

// SMTP Server
const smtpServer = new SMTPServer({
  banner: 'Music School Mock Email Server',
  authOptional: true,
  onConnect(session, callback) {
    console.log(`[SMTP] New connection from ${session.remoteAddress}`);
    callback();
  },
  onMailFrom(address, session, callback) {
    console.log(`[SMTP] Mail from: ${address.address}`);
    callback();
  },
  onRcptTo(address, session, callback) {
    console.log(`[SMTP] Mail to: ${address.address}`);
    callback();
  },
  onData(stream, session, callback) {
    console.log(`[SMTP] Receiving email data...`);
    
    let emailData = '';
    stream.on('data', (chunk) => {
      emailData += chunk.toString();
    });
    
    stream.on('end', async () => {
      try {
        const parsed = await simpleParser(emailData);
        const email = {
          id: uuidv4(),
          from: parsed.from?.text || 'unknown@example.com',
          to: parsed.to?.text || 'unknown@example.com',
          subject: parsed.subject || 'No Subject',
          text: parsed.text || '',
          html: parsed.html || '',
          date: new Date().toISOString(),
          headers: parsed.headers,
          attachments: parsed.attachments || []
        };
        
        emails.unshift(email); // Add to beginning
        emailStats.totalReceived++;
        emailStats.lastReceived = new Date().toISOString();
        
        // Keep only last 1000 emails
        if (emails.length > 1000) {
          emails = emails.slice(0, 1000);
        }
        
        console.log(`[SMTP] Email received: ${email.subject} from ${email.from} to ${email.to}`);
        callback();
      } catch (error) {
        console.error('[SMTP] Error parsing email:', error);
        callback(error);
      }
    });
  }
});

// API Routes
app.get('/health', (req, res) => {
  res.json({
    status: 'healthy',
    service: 'Music School Mock Email Server',
    uptime: process.uptime(),
    stats: emailStats
  });
});

app.get('/api/emails', (req, res) => {
  const { limit = 50, offset = 0 } = req.query;
  const start = parseInt(offset);
  const end = start + parseInt(limit);
  
  res.json({
    emails: emails.slice(start, end),
    total: emails.length,
    stats: emailStats
  });
});

app.get('/api/emails/:id', (req, res) => {
  const email = emails.find(e => e.id === req.params.id);
  if (!email) {
    return res.status(404).json({ error: 'Email not found' });
  }
  res.json(email);
});

app.delete('/api/emails/:id', (req, res) => {
  const index = emails.findIndex(e => e.id === req.params.id);
  if (index === -1) {
    return res.status(404).json({ error: 'Email not found' });
  }
  emails.splice(index, 1);
  res.json({ message: 'Email deleted successfully' });
});

app.delete('/api/emails', (req, res) => {
  emails = [];
  emailStats.totalReceived = 0;
  emailStats.lastReceived = null;
  res.json({ message: 'All emails cleared' });
});

app.get('/api/stats', (req, res) => {
  res.json({
    totalEmails: emails.length,
    stats: emailStats,
    recentEmails: emails.slice(0, 10).map(email => ({
      id: email.id,
      from: email.from,
      to: email.to,
      subject: email.subject,
      date: email.date
    }))
  });
});

// Web interface
app.get('/', (req, res) => {
  res.send(`
    <!DOCTYPE html>
    <html>
    <head>
        <title>Music School Mock Email Server</title>
        <style>
            body { font-family: Arial, sans-serif; margin: 40px; }
            .header { background: #2c3e50; color: white; padding: 20px; border-radius: 5px; }
            .stats { background: #ecf0f1; padding: 20px; margin: 20px 0; border-radius: 5px; }
            .email { border: 1px solid #bdc3c7; margin: 10px 0; padding: 15px; border-radius: 5px; }
            .email-header { font-weight: bold; color: #2c3e50; }
            .email-meta { color: #7f8c8d; font-size: 0.9em; }
            button { background: #3498db; color: white; border: none; padding: 10px 20px; border-radius: 3px; cursor: pointer; }
            button:hover { background: #2980b9; }
        </style>
    </head>
    <body>
        <div class="header">
            <h1>🎵 Music School Mock Email Server</h1>
            <p>Development and Testing Email Server</p>
        </div>
        
        <div class="stats">
            <h3>📊 Statistics</h3>
            <p><strong>Total Emails:</strong> <span id="totalEmails">${emails.length}</span></p>
            <p><strong>Last Received:</strong> <span id="lastReceived">${emailStats.lastReceived || 'Never'}</span></p>
            <button onclick="loadEmails()">🔄 Refresh</button>
            <button onclick="clearAllEmails()">🗑️ Clear All</button>
        </div>
        
        <div id="emails">
            <h3>📧 Recent Emails</h3>
            <div id="emailsList">
                ${emails.slice(0, 10).map(email => `
                    <div class="email">
                        <div class="email-header">${email.subject}</div>
                        <div class="email-meta">
                            From: ${email.from} | To: ${email.to} | ${new Date(email.date).toLocaleString()}
                        </div>
                        <div style="margin-top: 10px; max-height: 100px; overflow: hidden;">
                            ${email.text.substring(0, 200)}${email.text.length > 200 ? '...' : ''}
                        </div>
                    </div>
                `).join('')}
            </div>
        </div>
        
        <script>
            function loadEmails() {
                fetch('/api/emails')
                    .then(response => response.json())
                    .then(data => {
                        document.getElementById('totalEmails').textContent = data.total;
                        document.getElementById('lastReceived').textContent = data.stats.lastReceived || 'Never';
                        
                        const emailsList = document.getElementById('emailsList');
                        emailsList.innerHTML = data.emails.map(email => \`
                            <div class="email">
                                <div class="email-header">\${email.subject}</div>
                                <div class="email-meta">
                                    From: \${email.from} | To: \${email.to} | \${new Date(email.date).toLocaleString()}
                                </div>
                                <div style="margin-top: 10px; max-height: 100px; overflow: hidden;">
                                    \${email.text.substring(0, 200)}\${email.text.length > 200 ? '...' : ''}
                                </div>
                            </div>
                        \`).join('');
                    });
            }
            
            function clearAllEmails() {
                if (confirm('Are you sure you want to clear all emails?')) {
                    fetch('/api/emails', { method: 'DELETE' })
                        .then(() => loadEmails());
                }
            }
            
            // Auto-refresh every 5 seconds
            setInterval(loadEmails, 5000);
        </script>
    </body>
    </html>
  `);
});

// Start servers
smtpServer.listen(SMTP_PORT, () => {
  console.log(`[SMTP] Mock SMTP server listening on port ${SMTP_PORT}`);
});

app.listen(PORT, () => {
  console.log(`[HTTP] Mock email server listening on port ${PORT}`);
  console.log(`[INFO] Web interface: http://localhost:${PORT}`);
  console.log(`[INFO] SMTP server: localhost:${SMTP_PORT}`);
  console.log(`[INFO] API endpoint: http://localhost:${PORT}/api/emails`);
});

// Graceful shutdown
process.on('SIGTERM', () => {
  console.log('[INFO] Shutting down mock email server...');
  smtpServer.close();
  process.exit(0);
});

process.on('SIGINT', () => {
  console.log('[INFO] Shutting down mock email server...');
  smtpServer.close();
  process.exit(0);
});
