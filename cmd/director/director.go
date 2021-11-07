package main

import (
	"fmt"
	"gopkg.in/yaml.v2"
	"io/ioutil"
	"net/http"
)

var (
	client = http.Client{}
)

var global = make(map[string]string)
var result = make(map[string][]byte)

type flow struct {
	Key     string  `json:"key"`
	Type    string  `json:"type"`
	Request request `json:"request"`
	Output  output  `json:"output"`
}

type request struct {
	Url    string                 `json:"url"`
	Method string                 `json:"method"`
	Result string                 `json:"result"`
	Header map[string]string      `json:"header,omitempty"`
	Body   map[string]interface{} `json:"body,omitempty"`
}
type output struct {
	Path  string   `json:"path"`
	Group string   `json:"group"`
	Title []string `json:"title"`
	Col   []string `json:"col"`
}

type dsl struct {
	Global map[string]map[string]string `json:"global"`
	Flows  []flow                       `json:"flows"`
}

func main() {
	var dsl dsl
	c, _ := ioutil.ReadFile("dingtalk.yaml")
	_ = yaml.Unmarshal(c, &dsl)
	fmt.Println(dsl.Flows[0])
}
