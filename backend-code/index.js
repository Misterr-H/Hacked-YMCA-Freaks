// initialise express for cors and json
const express = require('express');
const cors = require('cors');
const app = express();
app.use(cors());
app.use(express.json());
const PORT = process.env.PORT || 5000;

const prompt_text = "Angela is a virtual assistant of Himanshu who talks to himanshu's friends. Angela is smart, intelligent, polite, caring and can answer all questions using openai intelligence. Angela is created using gpt 3 ai model. \n\nFriend: Hi\nAngela: Hi, Angela is here. Himanshu's virtual assistant. How can I help?\nFriend: Where's Himanshu?\nAngela: Himanshu sir seems busy with coding stuff.\nFriend:"


const { Configuration, OpenAIApi } = require("openai");

const configuration = new Configuration({
  apiKey: "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",
});
const openai = new OpenAIApi(configuration);

// const response = await openai.createCompletion({
//   model: "text-davinci-002",
//   temperature: 0.7,
//   max_tokens: 256,
//   top_p: 1,
//   frequency_penalty: 0,
//   presence_penalty: 0,
// });

//setup express listener for autoresponder for wa
app.post('/angela', async (req, res) => {
    const message = req.body.query.message;
    console.log(message);
    const prompt = prompt_text + message + "\nAngela:";
    console.log(prompt);
    const response = await openai.createCompletion({
        model: "text-davinci-002",
        temperature: 0.9,
        max_tokens: 256,
        top_p: 1,
        frequency_penalty: 0,
        presence_penalty: 0.6,
        prompt: prompt,
        stop: [" Friend:", " Angela:"],
        suffix: ""
    });
    console.log(response.data);
    if(response.data.choices[0].text.trim()==="")
    res.status(200).json({
        "replies":[
            {
                "message": "Angela:" + "Didn't get you!"
            }
        ]
    });
    else
res.status(200).json({
        "replies":[
            {
                "message": "Angela:" + response.data.choices[0].text.trim()
            }
        ]
    });
    });

// start express server
app.listen(PORT, () => {
    console.log('Server started on port 4000');
}
);
