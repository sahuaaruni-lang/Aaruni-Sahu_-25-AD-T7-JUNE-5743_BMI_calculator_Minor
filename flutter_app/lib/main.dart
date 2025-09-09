import 'package:flutter/material.dart';

void main() {
  runApp(const BMIApp());
}

class BMIApp extends StatelessWidget {
  const BMIApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      title: 'BMI Calculator',
      theme: ThemeData(
        colorScheme: ColorScheme.fromSeed(seedColor: Colors.pink),
        useMaterial3: true,
      ),
      home: const BMIScreen(),
    );
  }
}

class BMIScreen extends StatefulWidget {
  const BMIScreen({super.key});

  @override
  State<BMIScreen> createState() => _BMIScreenState();
}

class _BMIScreenState extends State<BMIScreen> {
  final _formKey = GlobalKey<FormState>();
  final _heightCtrl = TextEditingController();
  final _weightCtrl = TextEditingController();

  double? _bmi;
  String? _category;

  @override
  void dispose() {
    _heightCtrl.dispose();
    _weightCtrl.dispose();
    super.dispose();
  }

  void _calculate() {
    if (!_formKey.currentState!.validate()) return;

    final height = double.parse(_heightCtrl.text) / 100; // cm → meters
    final weight = double.parse(_weightCtrl.text);

    final bmi = weight / (height * height);

    setState(() {
      _bmi = double.parse(bmi.toStringAsFixed(1));
      if (_bmi! < 18.5) {
        _category = "Underweight";
      } else if (_bmi! < 25) {
        _category = "Normal";
      } else if (_bmi! < 30) {
        _category = "Overweight";
      } else {
        _category = "Obese";
      }
    });
  }

  void _reset() {
    _heightCtrl.clear();
    _weightCtrl.clear();
    setState(() {
      _bmi = null;
      _category = null;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text("BMI Calculator")),
      body: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            children: [
              TextFormField(
                controller: _heightCtrl,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: "Height (cm)",
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) return "Enter height";
                  final h = double.tryParse(value);
                  if (h == null || h <= 0) return "Enter valid height";
                  return null;
                },
              ),
              const SizedBox(height: 16),
              TextFormField(
                controller: _weightCtrl,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: "Weight (kg)",
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.isEmpty) return "Enter weight";
                  final w = double.tryParse(value);
                  if (w == null || w <= 0) return "Enter valid weight";
                  return null;
                },
              ),
              const SizedBox(height: 24),
              Row(
                children: [
                  Expanded(
                    child: ElevatedButton(
                      onPressed: _calculate,
                      child: const Text("Calculate"),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: OutlinedButton(
                      onPressed: _reset,
                      child: const Text("Reset"),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 24),
              if (_bmi != null)
                Card(
                  elevation: 2,
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      children: [
                        Text("Your BMI: $_bmi",
                            style: const TextStyle(
                                fontSize: 20, fontWeight: FontWeight.bold)),
                        const SizedBox(height: 8),
                        Text("Category: $_category",
                            style: const TextStyle(fontSize: 18)),
                      ],
                    ),
                  ),
                ),
            ],
          ),
        ),
      ),
    );
  }
}
